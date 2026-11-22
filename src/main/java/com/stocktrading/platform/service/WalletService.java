package com.stocktrading.platform.service;

import com.stocktrading.platform.dto.WalletBalanceResponse;
import com.stocktrading.platform.dto.WalletTransactionResponse;
import com.stocktrading.platform.entity.TradeOrder;
import com.stocktrading.platform.entity.Wallet;
import com.stocktrading.platform.entity.WalletTransaction;
import com.stocktrading.platform.enums.TransactionType;
import com.stocktrading.platform.repository.WalletRepository;
import com.stocktrading.platform.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional(readOnly = true)
    public WalletBalanceResponse getBalance(String email) {

        Wallet wallet = walletRepository.findByUser_Email(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wallet not found"));

        return new WalletBalanceResponse(
                wallet.getId(),
                wallet.getBalance(),
                "INR"
        );
    }

    @Transactional
    public WalletBalanceResponse addMoney(String email, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }

        Wallet wallet = getWalletForUpdate(email);

        wallet.setBalance(wallet.getBalance().add(amount));

        walletRepository.save(wallet);

        saveTransaction(
                wallet,
                TransactionType.CREDIT,
                amount,
                null
        );

        return new WalletBalanceResponse(
                wallet.getId(),
                wallet.getBalance(),
                "INR"
        );
    }

    @Transactional(readOnly = true)
    public List<WalletTransactionResponse> getTransactions(String email) {

        Wallet wallet = walletRepository.findByUser_Email(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wallet not found"));

        return walletTransactionRepository
                .findByWallet_IdOrderByCreatedAtDesc(wallet.getId())
                .stream()
                .map(transaction -> new WalletTransactionResponse(
                        transaction.getId(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getCreatedAt()
                ))
                .toList();
    }

    // Will be used by the BUY order service in Phase 7.
    @Transactional
    public void debitForBuy(
            String email,
            BigDecimal amount,
            TradeOrder tradeOrder
    ) {

        applyTransaction(email, amount, TransactionType.DEBIT, tradeOrder);
    }

    // Will be used by the SELL order service in Phase 8.
    @Transactional
    public void creditForSell(
            String email,
            BigDecimal amount,
            TradeOrder tradeOrder
    ) {

        applyTransaction(email, amount, TransactionType.CREDIT, tradeOrder);
    }

    private void applyTransaction(
            String email,
            BigDecimal amount,
            TransactionType transactionType,
            TradeOrder tradeOrder
    ) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }

        Wallet wallet = getWalletForUpdate(email);

        if (transactionType == TransactionType.DEBIT
                && wallet.getBalance().compareTo(amount) < 0) {

            throw new IllegalArgumentException("Insufficient wallet balance");
        }

        BigDecimal newBalance;

        if (transactionType == TransactionType.DEBIT) {
            newBalance = wallet.getBalance().subtract(amount);
        } else {
            newBalance = wallet.getBalance().add(amount);
        }

        wallet.setBalance(newBalance);
        walletRepository.save(wallet);

        saveTransaction(
                wallet,
                transactionType,
                amount,
                tradeOrder
        );
    }

    private Wallet getWalletForUpdate(String email) {

        return walletRepository.findByUserEmailForUpdate(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("Wallet not found"));
    }

    private void saveTransaction(
            Wallet wallet,
            TransactionType transactionType,
            BigDecimal amount,
            TradeOrder tradeOrder
    ) {

        WalletTransaction transaction = new WalletTransaction();

        transaction.setWallet(wallet);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(wallet.getBalance());
        transaction.setTradeOrder(tradeOrder);

        walletTransactionRepository.save(transaction);
    }
}
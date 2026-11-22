package com.stocktrading.platform.dto;

import com.stocktrading.platform.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record WalletTransactionResponse(
        Long transactionId,
        TransactionType transactionType,
        BigDecimal amount,
        BigDecimal balanceAfter,
        LocalDateTime createdAt
) {
}
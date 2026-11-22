package com.stocktrading.platform.controller;

import com.stocktrading.platform.dto.AddMoneyRequest;
import com.stocktrading.platform.dto.WalletBalanceResponse;
import com.stocktrading.platform.dto.WalletTransactionResponse;
import com.stocktrading.platform.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/balance")
    public WalletBalanceResponse getBalance(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return walletService.getBalance(userDetails.getUsername());
    }


    @PostMapping("/add-money")
    public WalletBalanceResponse addMoney(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddMoneyRequest request
    ) {

        return walletService.addMoney(
                userDetails.getUsername(),
                request.amount()
        );
    }

    @GetMapping("/transactions")
    public List<WalletTransactionResponse> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        return walletService.getTransactions(userDetails.getUsername());
    }
}
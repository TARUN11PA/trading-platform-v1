package com.stocktrading.platform.dto;

import java.math.BigDecimal;

public record WalletBalanceResponse(
        Long walletId,
        BigDecimal balance,
        String currency
) {
}
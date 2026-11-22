
package com.stocktrading.platform.service;

import com.stocktrading.platform.dto.StockMarketResponse;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MarketPriceCache {

    private final Map<String, StockMarketResponse> priceCache =
            new ConcurrentHashMap<>();

    // Store or update stock price
    public void updatePrice(StockMarketResponse stock) {

        if (stock == null || stock.getSymbol() == null) {
            return;
        }

        priceCache.put(stock.getSymbol(), stock);
    }

    // Get price of a stock
    public StockMarketResponse getPrice(String symbol) {
        return priceCache.get(symbol);
    }

    // Get all cached stock prices
    public Collection<StockMarketResponse> getAllPrices() {
        return priceCache.values();
    }
}

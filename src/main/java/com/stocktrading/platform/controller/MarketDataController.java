
package com.stocktrading.platform.controller;

import com.stocktrading.platform.dto.StockMarketResponse;
import com.stocktrading.platform.dto.StockSearchResponse;
import com.stocktrading.platform.service.MarketDataService;
import com.stocktrading.platform.service.MarketPriceCache;
import com.stocktrading.platform.service.MarketPriceScheduler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/public/market")
public class MarketDataController {

    private final MarketDataService marketDataService;
    private final MarketPriceCache marketPriceCache;
    private final MarketPriceScheduler marketPriceScheduler;

    public MarketDataController(
            MarketDataService marketDataService,
            MarketPriceCache marketPriceCache,
            MarketPriceScheduler marketPriceScheduler) {

        this.marketDataService = marketDataService;
        this.marketPriceCache = marketPriceCache;
        this.marketPriceScheduler = marketPriceScheduler;
    }

    // Fetch stock details directly from IndianAPI
    @GetMapping("/stock")
    public StockMarketResponse getStockData(
            @RequestParam String name) {

        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock name cannot be empty"
            );
        }

        return marketDataService.getStockData(name.trim());
    }

    // Search Equity stocks
    @GetMapping("/search")
    public List<StockSearchResponse> searchStocks(
            @RequestParam String query) {

        if (query == null || query.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Search query cannot be empty"
            );
        }

        return marketDataService.searchStocks(query.trim());
    }

    // Get all cached market prices
    @GetMapping("/prices")
    public Collection<StockMarketResponse> getAllMarketPrices() {
        return marketPriceCache.getAllPrices();
    }

    // Get cached price of a specific stock
    @GetMapping("/price")
    public StockMarketResponse getStockPrice(
            @RequestParam String symbol) {

        if (symbol == null || symbol.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Stock symbol cannot be empty"
            );
        }

        StockMarketResponse stock =
                marketPriceCache.getPrice(symbol.trim());

        if (stock == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Stock price not found in cache"
            );
        }

        return stock;
    }

    // Manually refresh all configured stock prices
    @PostMapping("/refresh")
    public String refreshMarketPrices() {

        marketPriceScheduler.updateAllMarketPrices();

        return "Market prices refreshed successfully";
    }
}

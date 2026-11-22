
package com.stocktrading.platform.service;

import com.stocktrading.platform.dto.StockMarketResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class MarketPriceScheduler {

    private final MarketDataService marketDataService;
    private final MarketPriceCache marketPriceCache;
    private final Map<String, String> marketStocks;

    private static final ZoneId MARKET_ZONE =
            ZoneId.of("Asia/Kolkata");

    private static final LocalTime MARKET_OPEN =
            LocalTime.of(9, 15);

    private static final LocalTime MARKET_CLOSE =
            LocalTime.of(15, 30);

    public MarketPriceScheduler(
            MarketDataService marketDataService,
            MarketPriceCache marketPriceCache,
            @Qualifier("marketStocks") Map<String, String> marketStocks) {

        this.marketDataService = marketDataService;
        this.marketPriceCache = marketPriceCache;
        this.marketStocks = marketStocks;
    }

    // Automatically refresh every 20 minutes
    @Scheduled(
            fixedRate = 1200000,
            initialDelay = 0
    )
    public void refreshMarketPrices() {

        ZonedDateTime now = ZonedDateTime.now(MARKET_ZONE);

        DayOfWeek day = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        // Skip weekends
        if (day == DayOfWeek.SATURDAY ||
                day == DayOfWeek.SUNDAY) {
            return;
        }

        // Run only during market hours
        if (time.isBefore(MARKET_OPEN) ||
                !time.isBefore(MARKET_CLOSE)) {
            return;
        }

        updateAllMarketPrices();
    }

    // Manually refresh all configured stock prices
    public void updateAllMarketPrices() {

        System.out.println(
                "========== MARKET REFRESH STARTED =========="
        );

        for (Map.Entry<String, String> entry :
                marketStocks.entrySet()) {

            String stockName = entry.getKey();
            String symbol = entry.getValue();

            try {

                StockMarketResponse stock =
                        marketDataService.getStockData(
                                stockName,
                                symbol
                        );

                System.out.println("Stock Name: " + stock.getStockName());
                System.out.println("Stock Symbol: " + stock.getSymbol());
                System.out.println("NSE Price: " + stock.getNsePrice());
                System.out.println("BSE Price: " + stock.getBsePrice());

                marketPriceCache.updatePrice(stock);

                System.out.println(
                        "Market price updated: " + stockName
                );

            } catch (Exception e) {

                System.err.println(
                        "Failed to update price for: " + stockName
                );

                e.printStackTrace();
            }
        }

        System.out.println(
                "========== MARKET REFRESH COMPLETED =========="
        );
    }
}

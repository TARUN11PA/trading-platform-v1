
package com.stocktrading.platform.service;

import com.stocktrading.platform.dto.StockMarketResponse;
import com.stocktrading.platform.dto.StockSearchResponse;
import com.stocktrading.platform.exceptions.MarketDataException;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MarketDataService {

    private final RestClient restClient;

    public MarketDataService(RestClient marketDataRestClient) {
        this.restClient = marketDataRestClient;
    }

    // Fetch stock details directly from IndianAPI
    public StockMarketResponse getStockData(String name) {
        return getStockData(name, null);
    }

    // Fetch stock details with configured default symbol
    public StockMarketResponse getStockData(
            String name,
            String defaultSymbol) {

        Map<String, Object> response;

        try {
            response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stock")
                            .queryParam("name", name)
                            .build())
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});

        } catch (Exception e) {
            throw new MarketDataException(
                    "Unable to fetch stock data from market provider"
            );
        }

        if (response == null) {
            throw new MarketDataException(
                    "Market provider returned empty data"
            );
        }

        Map<String, Object> currentPrice =
                (Map<String, Object>) response.get("currentPrice");

        Map<String, Object> stockDetails =
                (Map<String, Object>) response.get("stockDetailsReusableData");

        StockMarketResponse stock = new StockMarketResponse();

        stock.setStockName(name);

        // Use configured symbol if available
        if (defaultSymbol != null && !defaultSymbol.isBlank()) {
            stock.setSymbol(defaultSymbol);
        } else {
            stock.setSymbol((String) response.get("exchangeCodeNse"));
        }

        if (currentPrice != null) {
            stock.setNsePrice(toDouble(currentPrice.get("NSE")));
            stock.setBsePrice(toDouble(currentPrice.get("BSE")));
        }

        if (stockDetails != null) {
            stock.setPreviousClose(toDouble(stockDetails.get("close")));

            stock.setChangePercentage(
                    toDouble(stockDetails.get("percentChange"))
            );

            stock.setDayHigh(toDouble(stockDetails.get("high")));
            stock.setDayLow(toDouble(stockDetails.get("low")));
            stock.setYearHigh(toDouble(stockDetails.get("yhigh")));
            stock.setYearLow(toDouble(stockDetails.get("ylow")));

            stock.setLastUpdated(
                    stockDetails.get("date") + " " +
                            stockDetails.get("time")
            );
        }

        return stock;
    }

    // Search only Equity stocks
    public List<StockSearchResponse> searchStocks(String query) {

        List<StockSearchResponse> stocks = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/industry_search")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<List<StockSearchResponse>>() {});

        if (stocks == null) {
            return List.of();
        }

        return stocks.stream()
                .filter(stock ->
                        "Equity".equalsIgnoreCase(stock.getStockType()))
                .collect(Collectors.toList());
    }

    // Convert API values into Double
    private Double toDouble(Object value) {

        if (value == null) {
            return null;
        }

        return Double.parseDouble(value.toString());
    }
}

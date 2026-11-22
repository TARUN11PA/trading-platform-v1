package com.stocktrading.platform.dto;

import lombok.Data;

@Data
public class StockMarketResponse {

    private String stockName;
    private String symbol;

    private Double nsePrice;
    private Double bsePrice;

    private Double previousClose;
    private Double changePercentage;

    private Double dayHigh;
    private Double dayLow;

    private Double yearHigh;
    private Double yearLow;

    private String lastUpdated;
}
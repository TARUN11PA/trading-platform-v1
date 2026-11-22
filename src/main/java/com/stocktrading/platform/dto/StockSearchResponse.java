
package com.stocktrading.platform.dto;

import lombok.Data;

@Data
public class StockSearchResponse {

    private String id;
    private String commonName;

    private String stockType;

    private String exchangeCodeBse;
    private String exchangeCodeNsi;

    private String bseRic;
    private String nseRic;
}

package com.stocktrading.platform.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MarketQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "stock_id", nullable = false, unique = true)
    private Stock stock;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal lastPrice;

    @Column(precision = 15, scale = 2)
    private BigDecimal previousClose;

    private LocalDateTime marketTimestamp;

    @Column(nullable = false)
    private LocalDateTime fetchedAt;
}
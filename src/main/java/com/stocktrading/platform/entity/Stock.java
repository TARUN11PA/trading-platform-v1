package com.stocktrading.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"symbol", "exchange"})
        }
)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String symbol;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 10)
    private String exchange;

    @Column(nullable = false, unique = true)
    private String instrumentKey;

    @Column(nullable = false)
    private boolean active = true;
}
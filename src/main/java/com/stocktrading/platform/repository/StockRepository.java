package com.stocktrading.platform.repository;

import com.stocktrading.platform.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySymbolAndExchange(String symbol, String exchange);

    Optional<Stock> findByInstrumentKey(String instrumentKey);

}
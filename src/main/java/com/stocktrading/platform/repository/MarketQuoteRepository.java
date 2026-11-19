package com.stocktrading.platform.repository;

import com.stocktrading.platform.entity.MarketQuote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketQuoteRepository extends JpaRepository<MarketQuote, Long> {

    Optional<MarketQuote> findByStockId(Long stockId);

}
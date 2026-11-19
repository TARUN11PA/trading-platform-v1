package com.stocktrading.platform.repository;

import com.stocktrading.platform.entity.TradeOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeOrderRepository extends JpaRepository<TradeOrder, Long> {

    List<TradeOrder> findByUserId(Long userId);

    List<TradeOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

}
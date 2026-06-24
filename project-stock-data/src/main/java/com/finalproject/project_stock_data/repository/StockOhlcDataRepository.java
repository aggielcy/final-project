package com.finalproject.project_stock_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finalproject.project_stock_data.entity.StockOhlcDataEntity;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;



@Repository
public interface StockOhlcDataRepository extends JpaRepository <StockOhlcDataEntity, Long>{
List <StockOhlcDataEntity> findByStockEntity_Id(Long stockId);
boolean existsByStockEntity_IdAndTradeDate(Long stockId, LocalDate tradeDate);
  
}

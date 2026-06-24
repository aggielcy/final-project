package com.finalproject.project_stock_data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finalproject.project_stock_data.entity.StockProfilesEntity;
import java.util.Optional;


@Repository
public interface StockProfilesRepository extends JpaRepository<StockProfilesEntity, Long> {
  Optional<StockProfilesEntity> findByStockEntity_Id(Long stockId);
}

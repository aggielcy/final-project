package com.finalproject.project_stock_data.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.finalproject.project_stock_data.entity.StocksEntity;

@Repository
public interface StocksRepository extends JpaRepository <StocksEntity, Long> {
  Optional<StocksEntity> findBySymbol(String symbol);
}

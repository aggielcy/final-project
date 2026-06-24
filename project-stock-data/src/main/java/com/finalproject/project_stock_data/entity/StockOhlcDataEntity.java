package com.finalproject.project_stock_data.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock_ohlc_data")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class StockOhlcDataEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String symbol;
  @Column (name = "trade_date", nullable = false)
  private LocalDate tradeDate;
  @Column(precision = 10, scale = 4, nullable = false)
  private BigDecimal high;
  @Column(precision = 10, scale = 4, nullable = false)
  private BigDecimal low;
  @Column(precision = 10, scale = 4, nullable = false)
  private BigDecimal open;
  @Column(precision = 10, scale = 4, nullable = false)
  private BigDecimal close;
  @Column(nullable = false)
  private Long volume;

  @ManyToOne
  @JoinColumn(name = "stock_id", nullable = false)
  @Setter
  private StocksEntity stockEntity;


  
}

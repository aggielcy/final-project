package com.finalproject.project_stock_data.entity;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.Getter;


@Entity
@Table(name = "stock_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class StockProfilesEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String industry;
  private BigDecimal marketCap;
  private String logo;
  private String companyName;
  private BigDecimal shareOutstanding;

  @OneToOne
  @JoinColumn(name = "stock_id", nullable = false)
  @Setter
  private StocksEntity stockEntity;


}

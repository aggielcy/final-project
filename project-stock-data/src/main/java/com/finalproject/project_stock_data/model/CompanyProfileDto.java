package com.finalproject.project_stock_data.model;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class CompanyProfileDto {
  private String name;
  private BigDecimal marketCapitalization;
  private String finnhubIndustry;
  private String logo;
  private BigDecimal shareOutstanding;

}

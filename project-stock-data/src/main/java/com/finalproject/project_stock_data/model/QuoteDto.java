package com.finalproject.project_stock_data.model;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class QuoteDto {
  private BigDecimal currentMarketPrice;
  private BigDecimal currentMarketChange;
  private BigDecimal currentMarketPriceChangePercentage;
  private BigDecimal currentTradevolume;

  
}

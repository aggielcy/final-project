package com.finalproject.project_data_provider.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuoteDTO {
  private BigDecimal currentMarketPrice;
  private BigDecimal currentMarketChange;
  private BigDecimal currentMarketPriceChangePercentage;
  private BigDecimal currentTradevolume;

  
}

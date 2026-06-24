package com.finalproject.project_heatmap_ui.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class StockOhlcDto {
  private Long stockId;
  private String symbol;
  private String companyName;
  private BigDecimal marketCap;
  private String industry;
  private BigDecimal shareOutstanding;
  private String logo;
  private List<StockOhlcData> stockOhlcDatas;
  private BigDecimal currentPriceChangePercent;

  @Getter
  public static class StockOhlcData {
    private LocalDate tradeDate;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private Long volume;
  }

  
}

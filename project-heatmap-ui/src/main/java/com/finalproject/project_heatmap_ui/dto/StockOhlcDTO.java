package com.finalproject.project_heatmap_ui.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockOhlcDTO {
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
  @Builder
  public static class StockOhlcData {
    private LocalDate tradeDate;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private Long volume;
  }

}

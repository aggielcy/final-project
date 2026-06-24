package com.finalproject.project_stock_data.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
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
  @Setter                                       //! ← add this
  private BigDecimal currentPriceChangePercent; //! ← add this

  @Builder
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class StockOhlcData {
    private LocalDate tradeDate;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal open;
    private BigDecimal close;
    private Long volume;
  }


}

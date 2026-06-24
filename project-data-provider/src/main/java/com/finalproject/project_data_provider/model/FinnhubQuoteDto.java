package com.finalproject.project_data_provider.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FinnhubQuoteDto {
  @JsonProperty(value = "c")
  private BigDecimal currentPrice;
  @JsonProperty(value = "d")
  private BigDecimal change;
  @JsonProperty(value = "dp")
  private BigDecimal percentChange;
  @JsonProperty(value = "h")
  private BigDecimal highPrice;
  @JsonProperty(value = "l")
  private BigDecimal lowPrice;
  @JsonProperty(value = "o")
  private BigDecimal openPrice;
  @JsonProperty(value = "pc")
  private BigDecimal previousClosePrice;
  @JsonProperty(value = "t")
  private Long timestamp;


}

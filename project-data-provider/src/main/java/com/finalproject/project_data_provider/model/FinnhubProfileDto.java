package com.finalproject.project_data_provider.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FinnhubProfileDto {
  private String ticker;
  private String name;
  private String country;
  private String currency;
  private String estimateCurrency;
  private String exchange;
  private LocalDate ipo;
  private BigDecimal marketCapitalization;
  private String logo;
  private BigDecimal shareOutstanding;
  private String finnhubIndustry;
  private String phone;
  @JsonProperty(value = "weburl")
  private String webUrl;
  private BigDecimal floatingShare; 

}

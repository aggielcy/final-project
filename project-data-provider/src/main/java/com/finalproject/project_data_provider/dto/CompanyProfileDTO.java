package com.finalproject.project_data_provider.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class CompanyProfileDTO {
  private String name;
  private BigDecimal marketCapitalization;
  private String finnhubIndustry;
  private String logo;
  private BigDecimal shareOutstanding;


}

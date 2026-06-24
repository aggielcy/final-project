package com.finalproject.project_heatmap_ui.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockEntityDTO {
  private List<String> symbol;
  
}

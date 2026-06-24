package com.finalproject.project_heatmap_ui.model;



import java.util.List;
import lombok.Getter;

@Getter
public class StockEntityDto {
  private List<StocksEntity> stockEntities;

  @Getter
  public static class StocksEntity{
  private Long id;
  private String name;
  private String symbol;
  private Boolean isActive;
  }



}

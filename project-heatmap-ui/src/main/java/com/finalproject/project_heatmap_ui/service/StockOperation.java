package com.finalproject.project_heatmap_ui.service;


import com.finalproject.project_heatmap_ui.dto.StockEntityDTO;
import com.finalproject.project_heatmap_ui.dto.StockOhlcDTO;

public interface StockOperation {

  StockOhlcDTO getStockOhlcDto(String symbol); // !
  StockEntityDTO getSymbol(); // !


  StockEntityDTO getSymbolFromCache();
  StockOhlcDTO getStockOhlcDtoFromCache(String symbol);



}

package com.finalproject.project_heatmap_ui.service;


import java.util.List;
import com.finalproject.project_heatmap_ui.dto.StockEntityDTO;
import com.finalproject.project_heatmap_ui.dto.StockOhlcDTO;

public interface StockOperation {

  StockOhlcDTO getStockOhlcDto(String symbol); // !
  StockEntityDTO getSymbol(); // !
  List<StockOhlcDTO> getAllStocksFromCache();//!

  StockEntityDTO getSymbolFromCache();
  StockOhlcDTO getStockOhlcDtoFromCache(String symbol);



}

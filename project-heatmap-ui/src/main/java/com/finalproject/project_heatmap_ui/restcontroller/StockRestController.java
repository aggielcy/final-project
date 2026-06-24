package com.finalproject.project_heatmap_ui.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.finalproject.project_heatmap_ui.dto.StockEntityDTO;
import com.finalproject.project_heatmap_ui.dto.StockOhlcDTO;
import com.finalproject.project_heatmap_ui.service.StockOperation;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class StockRestController {
  @Autowired
  private StockOperation stockOperation;

  @GetMapping("/ui/symbols")
  public StockEntityDTO getSymbol() {
      return this.stockOperation.getSymbol();
  }

  @GetMapping("/ui/ohlc")
  public StockOhlcDTO getStockOhlc(@RequestParam String symbol) {
      return this.stockOperation.getStockOhlcDto(symbol);
  }
  
  
}

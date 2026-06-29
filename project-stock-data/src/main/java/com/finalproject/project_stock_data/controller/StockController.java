package com.finalproject.project_stock_data.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import com.finalproject.project_stock_data.dto.CompanyProfileDTO;
import com.finalproject.project_stock_data.dto.QuoteDTO;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StocksEntity;

public interface StockController {

  // Retrieve OHLCV data per stock from DB
  @GetMapping("/symbol/{symbol}")
  StockOhlcDTO getStockBySymbol(@PathVariable String symbol);


  // Retrieve all stock company profile data (calls data-provider-app)
  @GetMapping("/data/profile")
  CompanyProfileDTO getCompanyDataDto(@RequestParam String symbol);

  // Retrieve all stock real-time quote data (calls data-provider-app)
  @GetMapping("/data/quote")
  QuoteDTO getQuote(@RequestParam String symbol);

  // Retrieve all stock symbols from DB
  @GetMapping("/data/symbol")
  List<StocksEntity> getSymbol();


  // frontend read redis only
  @GetMapping("/cache/ohlc/{symbol}")
  StockOhlcDTO getStockFromCache(@PathVariable String symbol);


  // since stopping scheduler, need to cache in redis one time through postman
  @GetMapping("/cache/allquote")
  void refreshAllQuotes();

  // since stopping scheduler, need to cache in redis one time through postman
  @GetMapping("/cache/allohlc")
  void refreshAllOhlc();
}



package com.finalproject.project_stock_data.controller.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.finalproject.project_stock_data.controller.StockController;
import com.finalproject.project_stock_data.dto.CompanyProfileDTO;
import com.finalproject.project_stock_data.dto.QuoteDTO;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.mapper.DtoMapper;
import com.finalproject.project_stock_data.model.CompanyProfileDto;
import com.finalproject.project_stock_data.model.QuoteDto;
import com.finalproject.project_stock_data.service.StockOperation;

@RestController
public class StockControllerImpl implements StockController {
  @Autowired
  private StockOperation stockOperation;
  @Autowired
  private DtoMapper dtoMapper;

  @Override
  public StockOhlcDTO getStockBySymbol(@PathVariable String symbol) {
    return stockOperation.getStockOhlcDto(symbol);
  }

  @Override
  public CompanyProfileDTO getCompanyDataDto(@RequestParam String symbol) {
    CompanyProfileDto profileDto =
        this.stockOperation.getCompanyDataDto(symbol);
    return this.dtoMapper.map(profileDto);
  }

  @Override
  public QuoteDTO getQuote(@RequestParam String symbol) {
    QuoteDto quoteDto = this.stockOperation.getQuote(symbol);
    return this.dtoMapper.map(quoteDto);
  }

  @Override
  public List<StocksEntity> getSymbol() {
    return this.stockOperation.getSymbol();
  }

  @Override
  public List<StockOhlcDTO> getAllStocksFromCache() {
    return this.stockOperation.getAllStocksFromCache();
  }


  @Override
  public StockOhlcDTO getStockFromCache(@PathVariable String symbol) {
    return this.stockOperation.getStockOhlcFromCache(symbol);
  }

  // !
  @Override
  public void refreshAllQuotes() {
    this.stockOperation.refreshAllQuotes();
  }

  // !
  @Override
  public void refreshAllOhlc() {
    this.stockOperation.refreshAllOhlc();
  }

}

package com.finalproject.project_stock_data.service;


import java.util.List;
import java.util.Map;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.model.CompanyProfileDto;
import com.finalproject.project_stock_data.model.QuoteDto;


public interface StockOperation {



  CompanyProfileDto getCompanyDataDto(String symbol);

  QuoteDto getQuote(String symbol);

  void refreshAllQuotes(); //!

  StockOhlcDTO getStockOhlcDto(String symbol);

  List <StocksEntity> getSymbol();


}

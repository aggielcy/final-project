package com.finalproject.project_stock_data.service;


import java.util.List;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.model.CompanyProfileDto;
import com.finalproject.project_stock_data.model.QuoteDto;


public interface StockOperation {



  CompanyProfileDto getCompanyDataDto(String symbol);

  QuoteDto getQuote(String symbol);

  void refreshAllQuotes(); //! to load all the data into redis to cache
  void refreshAllOhlc(); //! to load all the data into redis to cache

  StockOhlcDTO getStockOhlcDto(String symbol);

  List <StocksEntity> getSymbol();

  StockOhlcDTO getStockOhlcFromCache(String symbol);

  //!to get all StockOhlcDTO from redis to load quicker
  List<StockOhlcDTO> getAllStocksFromCache();


}

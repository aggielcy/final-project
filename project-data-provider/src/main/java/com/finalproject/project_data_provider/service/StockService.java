package com.finalproject.project_data_provider.service;

import com.finalproject.project_data_provider.model.FinnhubProfileDto;
import com.finalproject.project_data_provider.model.FinnhubQuoteDto;

public interface StockService {
FinnhubProfileDto getCompanyData(String symbol);
FinnhubQuoteDto getQuote(String symbol);
  
}

package com.finalproject.project_data_provider.controller;

import org.springframework.web.bind.annotation.RestController;
import com.finalproject.project_data_provider.dto.CompanyProfileDTO;
import com.finalproject.project_data_provider.dto.QuoteDTO;
import com.finalproject.project_data_provider.model.FinnhubProfileDto;
import com.finalproject.project_data_provider.model.FinnhubQuoteDto;
import com.finalproject.project_data_provider.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class Controller {
  @Autowired
  private StockService stockService;


  @GetMapping("/provider/profile")
  public CompanyProfileDTO getCompanyDataDto(@RequestParam String symbol) {
    FinnhubProfileDto profileDto = this.stockService.getCompanyData(symbol);
    return CompanyProfileDTO.builder() //
    .name(profileDto.getName())//
    .marketCapitalization(profileDto.getMarketCapitalization())//
    .finnhubIndustry(profileDto.getFinnhubIndustry())//
    .logo(profileDto.getLogo())//
    .shareOutstanding(profileDto.getShareOutstanding())//
    .build();
  }


  @GetMapping("/provider/quote")
  public QuoteDTO getQuote(@RequestParam String symbol) {
    FinnhubQuoteDto quoteDto = this.stockService.getQuote(symbol);
    return QuoteDTO.builder()//
    .currentMarketPrice(quoteDto.getCurrentPrice())//
    .currentMarketChange(quoteDto.getChange())//
    .currentMarketPriceChangePercentage(quoteDto.getPercentChange())//
    .currentTradevolume(null) //!
    .build();
  }

}

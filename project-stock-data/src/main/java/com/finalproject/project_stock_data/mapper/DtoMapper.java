package com.finalproject.project_stock_data.mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.finalproject.project_stock_data.dto.CompanyProfileDTO;
import com.finalproject.project_stock_data.dto.QuoteDTO;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StockOhlcDataEntity;
import com.finalproject.project_stock_data.entity.StockProfilesEntity;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.model.CompanyProfileDto;
import com.finalproject.project_stock_data.model.QuoteDto;

@Component
public class DtoMapper {

  public StockOhlcDTO map(StocksEntity stockEntity, StockProfilesEntity profile,
      List<StockOhlcDTO.StockOhlcData> ohlcDataList) {
    return StockOhlcDTO.builder().stockId(stockEntity.getId())
        .symbol(stockEntity.getSymbol()).companyName(profile.getCompanyName())
        .marketCap(profile.getMarketCap()).industry(profile.getIndustry())
        .shareOutstanding(profile.getShareOutstanding()).logo(profile.getLogo())
        .stockOhlcDatas(ohlcDataList).build();
  }

  // public StockOhlcDto.StockOhlcData mapOhlcData(StockOhlcDataEntity entity) {
  // return StockOhlcDto.StockOhlcData.builder()
  // .tradeDate(entity.getTradeDate())
  // .high(entity.getHigh())
  // .low(entity.getLow())
  // .open(entity.getOpen())
  // .close(entity.getClose())
  // .volume(entity.getVolume())
  // .build();
  // }

  public CompanyProfileDTO map(CompanyProfileDto profileDto) {
    return CompanyProfileDTO.builder() //
        .name(profileDto.getName())//
        .marketCapitalization(profileDto.getMarketCapitalization())//
        .finnhubIndustry(profileDto.getFinnhubIndustry())//
        .logo(profileDto.getLogo())//
        .shareOutstanding(profileDto.getShareOutstanding())//
        .build();
  }

  
  public QuoteDTO map(QuoteDto quoteDto) {
    return QuoteDTO.builder()//
        .currentMarketPrice(quoteDto.getCurrentMarketPrice())//
        .currentMarketChange(quoteDto.getCurrentMarketChange())//
        .currentMarketPriceChangePercentage(
            quoteDto.getCurrentMarketPriceChangePercentage())//
        .currentTradevolume(null) // !
        .build();
  }


}

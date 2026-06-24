package com.finalproject.project_heatmap_ui.service.impl;


import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.finalproject.project_heatmap_ui.dto.StockEntityDTO;
import com.finalproject.project_heatmap_ui.dto.StockOhlcDTO;
import com.finalproject.project_heatmap_ui.model.StockEntityDto;
import com.finalproject.project_heatmap_ui.model.StockEntityDto.StocksEntity;
import com.finalproject.project_heatmap_ui.model.StockOhlcDto;
import com.finalproject.project_heatmap_ui.service.StockOperation;

@Service
public class StockDataService implements StockOperation {
  @Autowired
  private RestTemplate restTemplate;
  @Value("${app.stock-data.domain}")
  private String domain;
  @Value("${app.stock-data.port}")
  private String port;


  // http://localhost:8080/symbol/AMZN
  @Override
  public StockOhlcDTO getStockOhlcDto(String symbol) {

    String url = UriComponentsBuilder.newInstance() //
        .scheme("http") //
        .host(this.domain) //
        .port(this.port)//
        .path("/symbol/" + symbol).toUriString();
    System.out.println("url=" + url);

    StockOhlcDto stockOhlcDto =
        this.restTemplate.getForObject(url, StockOhlcDto.class);

    List<StockOhlcDTO.StockOhlcData> ohlcDataList = stockOhlcDto.getStockOhlcDatas().stream()
            .map(e -> StockOhlcDTO.StockOhlcData.builder()
                .tradeDate(e.getTradeDate())
                .high(e.getHigh())
                .low(e.getLow())
                .open(e.getOpen())
                .close(e.getClose())
                .volume(e.getVolume())
                .build())
            .toList();
    

    return StockOhlcDTO.builder()//
    .stockId(stockOhlcDto.getStockId()) //
        .symbol(stockOhlcDto.getSymbol()) //
        .companyName(stockOhlcDto.getCompanyName()) //
        .marketCap(stockOhlcDto.getMarketCap()) //
        .industry(stockOhlcDto.getIndustry())
        .shareOutstanding(stockOhlcDto.getShareOutstanding())//
        .logo(stockOhlcDto.getLogo())
        .stockOhlcDatas(ohlcDataList)
        .currentPriceChangePercent(stockOhlcDto.getCurrentPriceChangePercent()).build();
  } // !

  // http://localhost:8101/data/symbol

  @Override
  public StockEntityDTO getSymbol() {

    String url = UriComponentsBuilder.newInstance() //
        .scheme("http") //
        .host(this.domain) //
        .port(this.port)//
        .path("/data/symbol") //
        .toUriString();
    System.out.println("url=" + url);

    List<StocksEntity> stockList = Arrays.asList(this.restTemplate
        .getForObject(url, StockEntityDto.StocksEntity[].class));

    List<String> symbols =
        stockList.stream().map(StocksEntity::getSymbol).toList();

    return StockEntityDTO.builder().symbol(symbols).build();
  }



}

package com.finalproject.project_data_provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.finalproject.project_data_provider.model.FinnhubProfileDto;
import com.finalproject.project_data_provider.model.FinnhubQuoteDto;
import com.finalproject.project_data_provider.service.StockService;

@Service
public class FinnhubService implements StockService {
  @Autowired
  private RestTemplate restTemplate;

  @Value(value = "${api.finnhub.domain}")
  private String domain;
  @Value(value = "${api.finnhub.vers}")
  private String vers;
  @Value(value = "${api.finnhub.endpoints.chart}")
  private String chartEndpoint;
  @Value(value = "${api.finnhub.key}")
  private String key;


  // http://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=d8nqe89r01qvvn9a3070d8nqe89r01qvvn9a307g
  // http://finnhub.io/api/v1/stock/profile2?symbol=AAPL&token=d8nqe89r01qvvn9a3070d8nqe89r01qvvn9a307g
  
  @Override
  public FinnhubProfileDto getCompanyData(String symbol) {
    String url = UriComponentsBuilder.newInstance() //
        .scheme("https") //
        .host(this.domain) //
        .path("/api/v1") //
        .path("/" + this.chartEndpoint) //
        .queryParam("symbol", symbol) //
        .queryParam("token", key) //
        .build() //
        .toUriString();
    System.out.println("url=" + url);
    return this.restTemplate.getForObject(url, FinnhubProfileDto.class);
  }


  // http://finnhub.io/api/v1/quote?symbol=AAPL&token=d8nqe89r01qvvn9a3070d8nqe89r01qvvn9a307g

  @Override
  public FinnhubQuoteDto getQuote(String symbol) {
    String url = UriComponentsBuilder.newInstance() //
        .scheme("https") //
        .host(this.domain) //
        .path("/api/v1/quote") //
        .queryParam("symbol", symbol) //
        .queryParam("token", key) //
        .build() //
        .toUriString();
    System.out.println("url=" + url);
    return this.restTemplate.getForObject(url,  FinnhubQuoteDto.class);
  }


}

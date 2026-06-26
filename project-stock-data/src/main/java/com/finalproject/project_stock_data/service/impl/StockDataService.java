package com.finalproject.project_stock_data.service.impl;


import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.finalproject.project_stock_data.dto.StockOhlcDTO;
import com.finalproject.project_stock_data.entity.StockProfilesEntity;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.lib.RedisHelper;
import com.finalproject.project_stock_data.mapper.DtoMapper;
import com.finalproject.project_stock_data.model.CompanyProfileDto;
import com.finalproject.project_stock_data.model.QuoteDto;
import com.finalproject.project_stock_data.repository.StockOhlcDataRepository;
import com.finalproject.project_stock_data.repository.StockProfilesRepository;
import com.finalproject.project_stock_data.repository.StocksRepository;
import com.finalproject.project_stock_data.service.StockOperation;

@Service
public class StockDataService implements StockOperation {
        @Autowired
        private StockOhlcDataRepository stockOhlcDataRepository;
        @Autowired
        private StocksRepository stocksRepository;
        @Autowired
        private StockProfilesRepository stockProfilesRepository;
        @Autowired
        private DtoMapper dtoMapper;
        @Autowired
        private RedisHelper redisHelper;
        @Autowired
        private RestTemplate restTemplate;
        @Value("${spring.app.data-provider.domain}")
        private String domain;
        @Value("${spring.app.data-provider.port}")
        private String port;


        // http://localhost:8080/provider/profile?symbol=AAPL

        @Override
        public CompanyProfileDto getCompanyDataDto(String symbol) {
                String url = UriComponentsBuilder.newInstance().scheme("http")
                                .host(this.domain) //
                                .port(this.port)//
                                .path("/provider/profile") //
                                .queryParam("symbol", symbol) //
                                .toUriString();
                System.out.println("url=" + url);
                CompanyProfileDto companyProfileDto = this.restTemplate
                                .getForObject(url, CompanyProfileDto.class);
                return companyProfileDto;
        }


        @Override
        public QuoteDto getQuote(String symbol) {
                String url = UriComponentsBuilder.newInstance() //
                                .scheme("http") //
                                .host(this.domain) //
                                .port(this.port)//
                                .path("/provider/quote") //
                                .queryParam("symbol", symbol) //
                                .build().toUriString();
                System.out.println("url=" + url);
                QuoteDto quoteDto = this.restTemplate.getForObject(url,
                                QuoteDto.class);
                return quoteDto;
        }

        // !
        @Override
        public void refreshAllQuotes() {
                List<StocksEntity> stocks = this.stocksRepository.findAll();

                for (StocksEntity stock : stocks) {
                        try {
                                QuoteDto quote = this
                                                .getQuote(stock.getSymbol());
                                if (quote != null && quote
                                                .getCurrentMarketPriceChangePercentage() != null) {
                                        this.redisHelper.set("pct:"
                                                        + stock.getSymbol(),
                                                        quote.getCurrentMarketPriceChangePercentage(),
                                                        Duration.ofDays(365)); //!
                                }
                                Thread.sleep(1200);
                        } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                        } catch (Exception e) {
                                // skip failed symbol
                        }
                }
        }


        @Override
        public List<StocksEntity> getSymbol() {
                return this.stocksRepository.findAll();
        }


        @Override
        public StockOhlcDTO getStockOhlcDto(String symbol) {
                String redisKey = "stock:" + symbol;

                // 1. Try Redis first
                StockOhlcDTO cachedDto = this.redisHelper.get(redisKey,
                                StockOhlcDTO.class);
                if (cachedDto != null) {
                        BigDecimal livePct = this.redisHelper
                                        .get("pct:" + symbol, BigDecimal.class);
                        if (livePct != null) {
                                cachedDto.setCurrentPriceChangePercent(livePct); //!
                        }
                        return cachedDto;
                }

                // 2. Cache miss -> query DB
                StocksEntity stockEntity = this.stocksRepository
                                .findBySymbol(symbol)
                                .orElseThrow(() -> new RuntimeException(
                                                "Stock not found, symbol = "
                                                                + symbol));

                Long stockId = stockEntity.getId();

                StockProfilesEntity profile = this.stockProfilesRepository
                                .findByStockEntity_Id(stockId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Stock profile not found, stockId = "
                                                                + stockId));

                List<StockOhlcDTO.StockOhlcData> ohlcDataList =
                                this.stockOhlcDataRepository
                                                .findByStockEntity_Id(stockId) //
                                                .stream()
                                                .map(entity -> StockOhlcDTO.StockOhlcData
                                                                .builder()
                                                                .tradeDate(entity
                                                                                .getTradeDate())
                                                                .high(entity.getHigh())
                                                                .low(entity.getLow())
                                                                .open(entity.getOpen())
                                                                .close(entity.getClose())
                                                                .volume(entity.getVolume())
                                                                .build())
                                                .toList();

                StockOhlcDTO stockohlcdto = this.dtoMapper.map(stockEntity,
                                profile, ohlcDataList);

                // 3. Save to Redis for 24 hours
                this.redisHelper.set(redisKey, stockohlcdto,
                                Duration.ofHours(24));

                //! 4. Set live % change
                BigDecimal livePct = this.redisHelper.get("pct:" + symbol,
                                BigDecimal.class);
                if (livePct != null) {
                        stockohlcdto.setCurrentPriceChangePercent(livePct);
                }

                // 5. Return result
                return stockohlcdto;

        }



}

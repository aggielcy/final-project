package com.finalproject.project_stock_data.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.finalproject.project_stock_data.entity.StockOhlcDataEntity;
import com.finalproject.project_stock_data.entity.StocksEntity;
import com.finalproject.project_stock_data.model.YahooChartResponse;
import com.finalproject.project_stock_data.model.YahooChartResponse.YahooQuote;
import com.finalproject.project_stock_data.model.YahooChartResponse.YahooResult;
import com.finalproject.project_stock_data.repository.StockOhlcDataRepository;
import com.finalproject.project_stock_data.repository.StocksRepository;
import com.finalproject.project_stock_data.service.OhlcUpdateService;

@Service
public class OhlcUpdateServiceImpl implements OhlcUpdateService {

    @Autowired
    private StocksRepository stocksRepository;

    @Autowired
    private StockOhlcDataRepository stockOhlcDataRepository;

    @Autowired
    private RestTemplate restTemplate;

    //!
    @Override
    public void updateLastNightOhlc() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        long period1 = yesterday.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
        long period2 = yesterday.plusDays(1).atStartOfDay(ZoneOffset.UTC).toEpochSecond();

        List<StocksEntity> stocks = stocksRepository.findAll();
        System.out.println("Starting OHLC update for " + yesterday);

        for (StocksEntity stock : stocks) {
            try {
                // Skip if data already exists
                if (stockOhlcDataRepository.existsByStockEntity_IdAndTradeDate(
                        stock.getId(), yesterday)) {
                    continue;
                }

                String url = "https://query1.finance.yahoo.com/v8/finance/chart/"
                        + stock.getSymbol()
                        + "?period1=" + period1
                        + "&period2=" + period2
                        + "&interval=1d&events=history";

                YahooChartResponse response = restTemplate.getForObject(
                        url, YahooChartResponse.class);

                if (response == null || response.getChart() == null
                        || response.getChart().getResult() == null
                        || response.getChart().getResult().isEmpty()) {
                    continue;
                }

                YahooResult result = response.getChart().getResult().get(0);
                List<Long> timestamps = result.getTimestamp();
                YahooQuote quote = result.getIndicators().getQuote().get(0);

                if (timestamps == null || timestamps.isEmpty()) continue;

                for (int i = 0; i < timestamps.size(); i++) {
                    LocalDate tradeDate = LocalDate.ofEpochDay(
                            timestamps.get(i) / 86400);

                    if (quote.getOpen().get(i) == null
                            || quote.getClose().get(i) == null) continue;

                    StockOhlcDataEntity entity = StockOhlcDataEntity.builder()
                            .symbol(stock.getSymbol())
                            .tradeDate(tradeDate)
                            .open(BigDecimal.valueOf(quote.getOpen().get(i)))
                            .high(BigDecimal.valueOf(quote.getHigh().get(i)))
                            .low(BigDecimal.valueOf(quote.getLow().get(i)))
                            .close(BigDecimal.valueOf(quote.getClose().get(i)))
                            .volume(quote.getVolume().get(i))
                            .build();

                    entity.setStockEntity(stock);
                    stockOhlcDataRepository.save(entity);
                    System.out.println("Saved OHLC for " + stock.getSymbol()
                            + " on " + tradeDate);
                }

                Thread.sleep(500);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("Failed for " + stock.getSymbol()
                        + ": " + e.getMessage());
            }
        }

        System.out.println("OHLC update complete for " + yesterday);
    }
}
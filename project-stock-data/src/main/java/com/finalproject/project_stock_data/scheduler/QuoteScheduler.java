package com.finalproject.project_stock_data.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.finalproject.project_stock_data.service.StockOperation;

@Component
public class QuoteScheduler {

    @Autowired
    private StockOperation stockOperation;

 //   @Scheduled(initialDelay = 0, fixedDelay = 300000)
    public void scheduleQuoteRefresh() {
        System.out.println("Starting quote refresh...");
        this.stockOperation.refreshAllQuotes();
        System.out.println("Quote refresh complete.");
    }
}
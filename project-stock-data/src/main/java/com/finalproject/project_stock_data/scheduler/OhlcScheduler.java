package com.finalproject.project_stock_data.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.finalproject.project_stock_data.service.OhlcUpdateService;

@Component
public class OhlcScheduler {
  @Autowired
  private OhlcUpdateService ohlcUpdateService;

 // @Scheduled(cron = "0 0 6 * * *")
  public void scheduleOhlcUpdate() {
    System.out.println("Starting nightly OHLC update...");
    this.ohlcUpdateService.updateLastNightOhlc();
    System.out.println("Nightly OHLC update complete.");
  }
}

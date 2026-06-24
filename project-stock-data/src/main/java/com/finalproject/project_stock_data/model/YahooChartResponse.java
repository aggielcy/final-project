package com.finalproject.project_stock_data.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YahooChartResponse {
    private YahooChart chart;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YahooChart {
        private List<YahooResult> result;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YahooResult {
        private List<Long> timestamp;
        private YahooIndicators indicators;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YahooIndicators {
        private List<YahooQuote> quote;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YahooQuote {
        private List<Double> open;
        private List<Double> high;
        private List<Double> low;
        private List<Double> close;
        private List<Long> volume;
    }
}
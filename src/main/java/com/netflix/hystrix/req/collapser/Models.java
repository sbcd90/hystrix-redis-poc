package com.netflix.hystrix.req.collapser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ToString
@AllArgsConstructor
@Data
class Ticker {
  String symbol;
}

@ToString
@AllArgsConstructor
@Data
class StockPrice {
  String symbol;
  Double price;
  Instant effectiveTime;
}

public class Models {
  List<StockPrice> stockPrices =
    Arrays.asList(new StockPrice("Ticker1", 1.0, Instant.ofEpochMilli(System.currentTimeMillis())),
      new StockPrice("Ticker2", 2.0, Instant.ofEpochMilli(System.currentTimeMillis())),
      new StockPrice("Ticker3", 3.0, Instant.ofEpochMilli(System.currentTimeMillis())),
      new StockPrice("Ticker4", 4.0, Instant.ofEpochMilli(System.currentTimeMillis())));

  public StockPrice load(Ticker stock) {
    for (StockPrice stockPrice: stockPrices) {
      if (stockPrice.getSymbol().equals(stock.getSymbol())) {
        return stockPrice;
      }
    }
    throw new RuntimeException("Stock price for stock - " + stock.getSymbol() + " not found");
  }

  public List<Pair<Ticker, StockPrice>> loadAll(List<Ticker> stocks) {
    List<Pair<Ticker, StockPrice>> foundStockPrices = new ArrayList<>();
    for (StockPrice stockPrice: stockPrices) {
      for (Ticker ticker: stocks) {
        if (stockPrice.getSymbol().equals(ticker.getSymbol())) {
          foundStockPrices.add(Pair.of(ticker, stockPrice));
        }
      }
    }
    return foundStockPrices;
  }
}


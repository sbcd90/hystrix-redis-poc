package com.netflix.hystrix.req.collapser;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class SimpleBatchingCommand extends HystrixCommand<List<Pair<Ticker, StockPrice>>> {

  private Models models;
  private List<Ticker> stocks;

  public SimpleBatchingCommand(Models models, List<Ticker> stocks) {
    super(HystrixCommandGroupKey.Factory.asKey("Stocks"));
    this.models = models;
    this.stocks = stocks;
  }

  @Override
  protected List<Pair<Ticker, StockPrice>> run() {
    CommandHitMonitor.incrementCommandHitValue("SimpleBatchingCommand");
    return models.loadAll(stocks);
  }
}
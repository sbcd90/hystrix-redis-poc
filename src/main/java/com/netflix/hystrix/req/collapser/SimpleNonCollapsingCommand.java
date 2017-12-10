package com.netflix.hystrix.req.collapser;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class SimpleNonCollapsingCommand extends HystrixCommand<StockPrice> {

  private final Models models;
  private final Ticker stock;

  public SimpleNonCollapsingCommand(Models models, Ticker stock) {
    super(HystrixCommandGroupKey.Factory.asKey("Stock"));
    this.models = models;
    this.stock = stock;
  }

  @Override
  protected StockPrice run() {
    CommandHitMonitor.incrementCommandHitValue("SimpleNonCollapsingCommand");
    return models.load(stock);
  }
}
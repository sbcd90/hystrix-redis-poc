package com.netflix.hystrix.req.collapser;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Future;

public class SimpleCollapsingCommandTest {

  @Before
  public void beforeEach() {
    CommandHitMonitor.createCommandMonitor(Pair.of("SimpleBatchingCommand", 0));
  }

  @Test
  public void testShouldCollapseRequestsWithSameStock() throws Exception {
    HystrixRequestContext context = HystrixRequestContext.initializeContext();
    try {
      Models models = new Models();
      SimpleCollapsingCommand simpleCollapsingCommand1 = new SimpleCollapsingCommand(models, new Ticker("Ticker1"));
      SimpleCollapsingCommand simpleCollapsingCommand2 = new SimpleCollapsingCommand(models, new Ticker("Ticker1"));

      Future<StockPrice> priceFuture1 = simpleCollapsingCommand1.queue();
      Future<StockPrice> priceFuture2 = simpleCollapsingCommand2.queue();

      StockPrice price1 = priceFuture1.get();
      StockPrice price2 = priceFuture2.get();

      assert price1.getSymbol().equals(price2.getSymbol());
      assert CommandHitMonitor.getCommandHitValue("SimpleBatchingCommand") == 1;
    } finally {
      context.shutdown();
    }
  }

  @Test
  public void testShouldCollapseRequestsWithDiffStock() throws Exception {
    HystrixRequestContext context = HystrixRequestContext.initializeContext();
    try {
      Models models = new Models();
      SimpleCollapsingCommand simpleCollapsingCommand1 = new SimpleCollapsingCommand(models, new Ticker("Ticker3"));
      SimpleCollapsingCommand simpleCollapsingCommand2 = new SimpleCollapsingCommand(models, new Ticker("Ticker4"));

      Future<StockPrice> priceFuture1 = simpleCollapsingCommand1.queue();
      Future<StockPrice> priceFuture2 = simpleCollapsingCommand2.queue();

      StockPrice price1 = priceFuture1.get();
      StockPrice price2 = priceFuture2.get();

      assert price1.getSymbol().equals("Ticker3");
      assert price2.getSymbol().equals("Ticker4");
      assert CommandHitMonitor.getCommandHitValue("SimpleBatchingCommand") == 1;
    } finally {
      context.shutdown();
    }
  }


}
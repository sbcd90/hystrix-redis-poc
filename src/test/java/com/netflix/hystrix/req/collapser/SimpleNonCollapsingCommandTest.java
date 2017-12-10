package com.netflix.hystrix.req.collapser;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Future;

public class SimpleNonCollapsingCommandTest {

  @Before
  public void beforeEach() {
    CommandHitMonitor.createCommandMonitor(Pair.of("SimpleNonCollapsingCommand", 0));
  }

  @Test
  public void testFetchPriceFromExternalService() {
    Models models = new Models();
    SimpleNonCollapsingCommand command = new SimpleNonCollapsingCommand(models, new Ticker("Ticker1"));
    StockPrice price = command.execute();
    assert price.getSymbol().equals("Ticker1");
    assert CommandHitMonitor.getCommandHitValue("SimpleNonCollapsingCommand") == 1;
  }

  @Test
  public void testShouldCallGatewayOnceInHystrix() {
    Models models = new Models();
    SimpleNonCollapsingCommand command = new SimpleNonCollapsingCommand(models, new Ticker("Ticker2"));
    StockPrice price = command.execute();
    assert price.getSymbol().equals("Ticker2");
    assert CommandHitMonitor.getCommandHitValue("SimpleNonCollapsingCommand") == 1;
  }

  @Test
  public void testShouldCallGatewayTwiceInHystrix() {
    Models models = new Models();
    SimpleNonCollapsingCommand command1 = new SimpleNonCollapsingCommand(models, new Ticker("Ticker3"));
    SimpleNonCollapsingCommand command2 = new SimpleNonCollapsingCommand(models, new Ticker("Ticker4"));
    StockPrice price1 = command1.execute();
    StockPrice price2 = command2.execute();

    assert price1.getSymbol().equals("Ticker3");
    assert price2.getSymbol().equals("Ticker4");
    assert CommandHitMonitor.getCommandHitValue("SimpleNonCollapsingCommand") == 2;
  }

  @Test
  public void testShouldCallGatewayTwiceInFuture() throws Exception {
    Models models = new Models();
    SimpleNonCollapsingCommand command1 = new SimpleNonCollapsingCommand(models, new Ticker("Ticker3"));
    SimpleNonCollapsingCommand command2 = new SimpleNonCollapsingCommand(models, new Ticker("Ticker4"));
    Future<StockPrice> priceFuture1 = command1.queue();
    Future<StockPrice> priceFuture2 = command2.queue();

    assert priceFuture1.get().getSymbol().equals("Ticker3");
    assert priceFuture2.get().getSymbol().equals("Ticker4");
    assert CommandHitMonitor.getCommandHitValue("SimpleNonCollapsingCommand") == 2;
  }
}
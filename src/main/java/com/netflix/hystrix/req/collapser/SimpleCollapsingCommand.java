package com.netflix.hystrix.req.collapser;

import com.netflix.hystrix.HystrixCollapser;
import com.netflix.hystrix.HystrixCollapserKey;
import com.netflix.hystrix.HystrixCollapserProperties;
import com.netflix.hystrix.HystrixCommand;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleCollapsingCommand extends HystrixCollapser<List<Pair<Ticker, StockPrice>>, StockPrice, Ticker> {

  private Models models;
  private Ticker stock;

  public SimpleCollapsingCommand(Models models, Ticker stock) {
    super(HystrixCollapser.Setter.withCollapserKey(HystrixCollapserKey.Factory.asKey("Stock"))
      .andCollapserPropertiesDefaults(HystrixCollapserProperties.Setter().withTimerDelayInMilliseconds(100)));
    this.models = models;
    this.stock = stock;
  }

  @Override
  public Ticker getRequestArgument() {
    return stock;
  }

  @Override
  protected HystrixCommand<List<Pair<Ticker, StockPrice>>> createCommand(Collection<CollapsedRequest<StockPrice, Ticker>> collection) {
    List<Ticker> stocks = collection.stream()
                          .map(CollapsedRequest::getArgument)
                          .collect(Collectors.toList());
    return new SimpleBatchingCommand(models, stocks);
  }

  @Override
  protected void mapResponseToRequests(List<Pair<Ticker, StockPrice>> pairs,
                                       Collection<CollapsedRequest<StockPrice, Ticker>> collection) {
    collection.forEach(request -> {
      Ticker ticker = request.getArgument();
      StockPrice stockPrice = pairs.stream()
                                .filter(pricePair -> pricePair.getKey().getSymbol().equals(ticker.getSymbol()))
                                .map(Pair::getValue)
                                .collect(Collectors.toList())
                                .get(0);
      request.setResponse(stockPrice);
    });
  }
}
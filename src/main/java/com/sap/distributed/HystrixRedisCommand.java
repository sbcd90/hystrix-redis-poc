package com.sap.distributed;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.sap.distributed.models.Product;
import redis.clients.jedis.Jedis;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HystrixRedisCommand extends HystrixCommand<List<Product>> {

  private final int num;

  public HystrixRedisCommand(int num) {
    super(HystrixCommandGroupKey.Factory.asKey("ProductKey"));
    this.num = num;
  }

  @Override
  protected List<Product> run() throws Exception {
    if (num == 1) {
      try {
        Client client = ClientBuilder.newClient();

        WebTarget webTarget =
          client.target("http://services.odata.org/V2/Northwind/Northwind.svc/");
        WebTarget productWebTarget =
          webTarget.path("Products").queryParam("$format", "json");
        Invocation.Builder invocationBuilder =
          productWebTarget.request(MediaType.APPLICATION_JSON);

        String response =
          invocationBuilder.get(String.class);

        JsonNode productsNode = new ObjectMapper().readTree(response).get("d").get("results");

        List<Product> products = new ArrayList<>();

        if (productsNode.isArray()) {
          for (JsonNode productNode: productsNode) {
            Product product = new Product();
            product.setCategoryId(productNode.get("CategoryID").asText());
            product.setProductId(productNode.get("ProductID").asText());
            product.setProductName(productNode.get("ProductName").asText());
            product.setQuantityPerUnit(productNode.get("QuantityPerUnit").asText());
            product.setSupplierId(productNode.get("SupplierID").asText());

            products.add(product);
          }
        }

        Jedis jedis = new Jedis("localhost", 6379);
        jedis.set("products", new ObjectMapper().writeValueAsString(products));
        return products;
      } catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      }
    } else {
      throw new RuntimeException("Throw exception");
    }
  }

  @Override
  protected List<Product> getFallback() {
    try {
      Jedis jedis = new Jedis("localhost", 6379);
      String response = jedis.get("products");
      return new ObjectMapper().readValue(response, new TypeReference<List<Product>>() {
      });
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
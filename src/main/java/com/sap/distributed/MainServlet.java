package com.sap.distributed;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.distributed.models.Product;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class MainServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req,
                       HttpServletResponse resp) throws ServletException, IOException {
    int start = Integer.valueOf(req.getParameter("start"));

    if (start == 1) {
      HystrixRedisCommand hystrixRedisCommand = new HystrixRedisCommand(1);
      List<Product> products = hystrixRedisCommand.execute();
      resp.setContentType("application/json");
      resp.getWriter().write(new ObjectMapper().writeValueAsString(products));
    } else {
      HystrixRedisCommand hystrixRedisCommand = new HystrixRedisCommand(start);
      List<Product> products = hystrixRedisCommand.execute();
      resp.setContentType("application/json");
      resp.getWriter().write(new ObjectMapper().writeValueAsString(products));
    }
    resp.getWriter().close();
  }
}
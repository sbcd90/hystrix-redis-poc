package com.sap.distributed.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Product implements Serializable {

  private static final long serialVersionUID = 1L;

  @JsonProperty("ProductID")
  private String productId;

  @JsonProperty("ProductName")
  private String productName;

  @JsonProperty("SupplierID")
  private String supplierId;

  @JsonProperty("CategoryID")
  private String categoryId;

  @JsonProperty("QuantityPerUnit")
  private String quantityPerUnit;

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getProductName() {
    return productName;
  }

  public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
  }

  public String getSupplierId() {
    return supplierId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setQuantityPerUnit(String quantityPerUnit) {
    this.quantityPerUnit = quantityPerUnit;
  }

  public String getQuantityPerUnit() {
    return quantityPerUnit;
  }
}
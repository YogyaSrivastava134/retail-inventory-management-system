package com.retail.ims.domain;

import java.math.BigDecimal;

public class Product {
    private final String sku;
    private String name;
    private BigDecimal price;
    private int reorderThreshold;
    private boolean active;

    public Product(String sku, String name, BigDecimal price, int reorderThreshold) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.reorderThreshold = reorderThreshold;
        this.active = true;
    }

    public String getSku() { return sku; }
    public String getName() { return name; }
    public BigDecimal getPrice() { return price; }
    public int getReorderThreshold() { return reorderThreshold; }
    public boolean isActive() { return active; }
}

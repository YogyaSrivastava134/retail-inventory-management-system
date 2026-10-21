package com.retail.ims.service;

import com.retail.ims.domain.Product;
import com.retail.ims.domain.StockItem;
import com.retail.ims.observer.StockObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockService {
    private final Map<String, Product> productCatalog = new ConcurrentHashMap<>();
    private final Map<String, StockItem> stockInventory = new ConcurrentHashMap<>();
    private final List<StockObserver> observers = new ArrayList<>();

    public void registerProduct(Product product, int initialStock) {
        productCatalog.put(product.getSku(), product);
        stockInventory.put(product.getSku(), new StockItem(product.getSku(), initialStock));
    }

    public void registerObserver(StockObserver observer) {
        this.observers.add(observer);
    }

    public synchronized void processOrderDeduction(String sku, int quantity) {
        StockItem stock = stockInventory.get(sku);
        Product product = productCatalog.get(sku);

        if (stock == null || product == null) {
            throw new IllegalArgumentException("SKU not found in catalog or inventory: " + sku);
        }

        // Reserve and permanently commit units
        stock.reserveStock(quantity);
        stock.commitReservation(quantity);

        // Check if remaining quantity triggers threshold alert
        if (stock.getAvailableQuantity() <= product.getReorderThreshold()) {
            notifyObservers(sku, stock.getAvailableQuantity(), product.getReorderThreshold());
        }
    }

    private void notifyObservers(String sku, int available, int threshold) {
        for (StockObserver obs : observers) {
            obs.onLowStockDetected(sku, available, threshold);
        }
    }

    public StockItem getStock(String sku) {
        return stockInventory.get(sku);
    }
}

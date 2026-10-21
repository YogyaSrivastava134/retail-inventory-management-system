package com.retail.ims.observer;

public interface StockObserver {
    void onLowStockDetected(String sku, int availableQuantity, int threshold);
}

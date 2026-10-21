package com.retail.ims.domain;

public class StockItem {
    private final String sku;
    private int availableQuantity;
    private int reservedQuantity;

    public StockItem(String sku, int initialQuantity) {
        this.sku = sku;
        this.availableQuantity = Math.max(0, initialQuantity);
        this.reservedQuantity = 0;
    }

    public synchronized void reserveStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Reserved quantity must be strictly positive.");
        }
        if (quantity > this.availableQuantity) {
            throw new IllegalStateException("Insufficient available inventory for SKU: " + sku);
        }
        this.availableQuantity -= quantity;
        this.reservedQuantity += quantity;
    }

    public synchronized void commitReservation(int quantity) {
        if (quantity > this.reservedQuantity) {
            throw new IllegalStateException("Cannot commit more than currently reserved.");
        }
        this.reservedQuantity -= quantity;
    }

    public synchronized void releaseReservation(int quantity) {
        if (quantity > this.reservedQuantity) {
            throw new IllegalStateException("Cannot release more than currently reserved.");
        }
        this.reservedQuantity -= quantity;
        this.availableQuantity += quantity;
    }

    public synchronized void addStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Added stock quantity must be positive.");
        }
        this.availableQuantity += quantity;
    }

    public String getSku() { return sku; }
    public synchronized int getAvailableQuantity() { return availableQuantity; }
    public synchronized int getReservedQuantity() { return reservedQuantity; }
}

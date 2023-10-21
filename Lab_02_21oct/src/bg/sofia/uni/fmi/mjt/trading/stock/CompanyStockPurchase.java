package bg.sofia.uni.fmi.mjt.trading.stock;

import java.time.LocalDateTime;

public abstract class CompanyStockPurchase implements StockPurchase {
    private int quantity;
    private LocalDateTime purchaseTimestamp;
    private double purchasePricePerUnit;

    public CompanyStockPurchase(int quantity, LocalDateTime purchaseTimestamp, double purchasePricePerUnit) {
        this.quantity = quantity;
        this.purchaseTimestamp = purchaseTimestamp;
        this.purchasePricePerUnit = purchasePricePerUnit;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public LocalDateTime getPurchaseTimestamp() {
        return purchaseTimestamp;
    }

    @Override
    public double getPurchasePricePerUnit() {
        return Math.round(purchasePricePerUnit * 100) / 100.0;
    }

    @Override
    public double getTotalPurchasePrice() {
        return Math.round(getPurchasePricePerUnit() * getQuantity() * 100) / 100.0;
    }

    @Override
    public abstract String getStockTicker();
}

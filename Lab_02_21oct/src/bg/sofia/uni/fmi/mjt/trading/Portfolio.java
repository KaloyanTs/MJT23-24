package bg.sofia.uni.fmi.mjt.trading;

import bg.sofia.uni.fmi.mjt.trading.price.PriceChartAPI;
import bg.sofia.uni.fmi.mjt.trading.stock.AmazonStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.GoogleStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.MicrosoftStockPurchase;
import bg.sofia.uni.fmi.mjt.trading.stock.StockPurchase;

import java.time.LocalDateTime;

public class Portfolio implements PortfolioAPI {

    private String owner;
    private StockPurchase[] stockPurchases;
    private double budget;
    private int maxSize;
    private PriceChartAPI priceChart;

    public Portfolio(String owner, PriceChartAPI priceChart, double budget, int maxSize) {
        this.owner = new String(owner);
        this.budget = budget;
        this.maxSize = maxSize;
        this.priceChart = priceChart;
        this.stockPurchases = new StockPurchase[0];
    }

    public Portfolio(String owner, PriceChartAPI priceChart, StockPurchase[] stockPurchases, double budget,
                     int maxSize) {
        this.owner = new String(owner);
        this.stockPurchases = stockPurchases;
        this.budget = budget;
        this.maxSize = maxSize;
        this.priceChart = priceChart;
    }

    @Override
    public StockPurchase buyStock(String stockTicker, int quantity) {
        if (stockTicker == null || this.stockPurchases.length == maxSize || quantity <= 0) return null;
        StockPurchase res;
        switch (stockTicker) {
            case "MSFT" -> {
                res = new MicrosoftStockPurchase(quantity, LocalDateTime.now(),
                        priceChart.getCurrentPrice(stockTicker));
            }
            case "AMZ" -> {
                res = new AmazonStockPurchase(quantity, LocalDateTime.now(),
                        priceChart.getCurrentPrice(stockTicker));
            }
            case "GOOG" -> {
                res = new GoogleStockPurchase(quantity, LocalDateTime.now(),
                        priceChart.getCurrentPrice(stockTicker));
            }
            default -> {
                return null;
            }
        }
        if (budget < res.getTotalPurchasePrice()) return null;
        budget -= res.getTotalPurchasePrice();
        StockPurchase[] stockPurchasesNew = new StockPurchase[this.stockPurchases.length + 1];
        for (int i = 0; i < this.stockPurchases.length; i++) {
            stockPurchasesNew[i] = this.stockPurchases[i];
        }
        stockPurchasesNew[this.stockPurchases.length] = res;
        this.stockPurchases = stockPurchasesNew;
        priceChart.changeStockPrice(stockTicker, 5);
        return res;
    }

    @Override
    public StockPurchase[] getAllPurchases() {
        return stockPurchases;
    }

    @Override
    public StockPurchase[] getAllPurchases(LocalDateTime startTimestamp, LocalDateTime endTimestamp) {
        int count = 0;
        for (StockPurchase s : stockPurchases) {
            if (s.getPurchaseTimestamp().isEqual(startTimestamp) || s.getPurchaseTimestamp().isEqual(endTimestamp)) {
                ++count;
                continue;
            }
            if (s.getPurchaseTimestamp().isAfter(startTimestamp) && s.getPurchaseTimestamp().isBefore(endTimestamp)) {
                ++count;
                continue;
            }
        }
        StockPurchase[] res = new StockPurchase[count];
        count = 0;
        for (StockPurchase s : stockPurchases) {
            if (s.getPurchaseTimestamp().isEqual(startTimestamp) || s.getPurchaseTimestamp().isEqual(endTimestamp)) {
                res[count++] = s;
                continue;
            }
            if (s.getPurchaseTimestamp().isAfter(startTimestamp) && s.getPurchaseTimestamp().isBefore(endTimestamp)) {
                res[count++] = s;
                continue;
            }
        }
        return res;
    }

    @Override
    public double getNetWorth() {
        double netWorth = 0;
        System.out.println("calc");
        for (StockPurchase s : stockPurchases)
            netWorth += s.getQuantity() * priceChart.getCurrentPrice(s.getStockTicker());
        return Math.round(netWorth * 100) / 100.0;
    }

    @Override
    public double getRemainingBudget() {
        return Math.round(budget * 100) / 100.0;
    }

    @Override
    public String getOwner() {
        return owner;
    }
}

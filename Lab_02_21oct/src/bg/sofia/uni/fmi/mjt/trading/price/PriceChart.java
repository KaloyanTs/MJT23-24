package bg.sofia.uni.fmi.mjt.trading.price;

public class PriceChart implements PriceChartAPI {

    private double microsoftStockPrice, googleStockPrice, amazonStockPrice;

    public PriceChart(double microsoftStockPrice, double googleStockPrice, double amazonStockPrice) {
        this.microsoftStockPrice = microsoftStockPrice;
        this.amazonStockPrice = amazonStockPrice;
        this.googleStockPrice = googleStockPrice;
    }

    @Override
    public double getCurrentPrice(String stockTicker) {
        if (stockTicker == null) return 0.0;
        switch (stockTicker) {
            case "MSFT" -> {
                return Math.round(microsoftStockPrice * 100) / 100.0;
            }
            case "AMZ" -> {
                return Math.round(amazonStockPrice * 100) / 100.0;
            }
            case "GOOG" -> {
                return Math.round(googleStockPrice * 100) / 100.0;
            }
            default -> {
                return 0.0;
            }
        }
    }

    @Override
    public boolean changeStockPrice(String stockTicker, int percentChange) {
        if (stockTicker == null || percentChange <= 0) return false;
        switch (stockTicker) {
            case "MSFT" -> {
                microsoftStockPrice += ((double) percentChange / 100) * microsoftStockPrice;
                return true;
            }
            case "AMZ" -> {
                amazonStockPrice += ((double) percentChange / 100) * amazonStockPrice;
                return true;
            }
            case "GOOG" -> {
                googleStockPrice += ((double) percentChange / 100) * googleStockPrice;
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}


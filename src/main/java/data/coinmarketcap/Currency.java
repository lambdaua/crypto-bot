package data.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Currency {

    private String id;
    private String name;
    private String symbol;
    private String priceUsd;
    @JsonProperty(value = "percent_change_24h")
    private String percentChange24H;
    @JsonProperty(value = "percent_change_7d")
    private String percentChange7d;
    @JsonProperty(value = "percent_change_1h")
    private String percentChange1H;
    private String lastUpdated;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public String getPercentChange24H() {
        return percentChange24H;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public String getPercentChange1H() {
        return percentChange1H;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public Currency() {
    }

    public Currency(String id, String name, String symbol, String priceUsd, String change24H, String change7H, String change1H, String lastUpdated) {

        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.priceUsd = priceUsd;
        this.percentChange24H = change24H;
        this.percentChange7d = change7H;
        this.percentChange1H = change1H;
        this.lastUpdated = lastUpdated;
    }



    @Override
    public String toString() {
        return "data.coinmarketcap.Currency{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", priceUsd='" + priceUsd + '\'' +
                ", percentChange24H='" + percentChange24H + '\'' +
                ", percentChange7d='" + percentChange7d + '\'' +
                ", percentChange1H='" + percentChange1H + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}

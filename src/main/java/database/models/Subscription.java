package database.models;

import java.util.Date;

public class Subscription {

    private String currency;
    private String diff;
    private Double lastRate;
    private Date lastUpdateTime;

    public Subscription(String currency, String diff, Double lastRate, Date lastUpdateTime) {

        this.currency = currency;
        this.diff = diff;
        this.lastRate = lastRate;
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDiff() {
        return diff;
    }

    public Double getLastRate() {
        return lastRate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }
}

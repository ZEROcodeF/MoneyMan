package com.team3jp.MoneyMan;

//For Database and for FixerAPI

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyRates {
    @SerializedName("timestamp")
    @Expose
    private long timestamp;
    @SerializedName("base")
    @Expose
    private String base;
    @SerializedName("rates")
    @Expose
    Rates rates;

    public CurrencyRates(long timestamp, Rates rates) {
        this.timestamp = timestamp;
        this.rates = rates;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Rates getRates() {
        return rates;
    }

    public void setRates(Rates rates) {
        this.rates = rates;
    }
}

class Rates {
    @SerializedName("AUD")
    @Expose
    private double AUD;
    @SerializedName("EUR")
    @Expose
    private double EUR;
    @SerializedName("GBP")
    @Expose
    private double GBP;
    @SerializedName("JPY")
    @Expose
    private double JPY;
    @SerializedName("USD")
    @Expose
    private double USD;
    @SerializedName("VND")
    @Expose
    private double VND;

    public Rates(double AUD, double EUR, double GBP, double JPY, double USD, double VND) {
        this.AUD = AUD;
        this.EUR = EUR;
        this.GBP = GBP;
        this.JPY = JPY;
        this.USD = USD;
        this.VND = VND;
    }

    public double getAUD() {
        return AUD;
    }

    public void setAUD(double AUD) {
        this.AUD = AUD;
    }


    public double getEUR() {
        return EUR;
    }

    public void setEUR(double EUR) {
        this.EUR = EUR;
    }

    public double getGBP() {
        return GBP;
    }

    public void setGBP(double GBP) {
        this.GBP = GBP;
    }

    public double getJPY() {
        return JPY;
    }

    public void setJPY(double JPY) {
        this.JPY = JPY;
    }

    public double getUSD() {
        return USD;
    }

    public void setUSD(double USD) {
        this.USD = USD;
    }

    public double getVND() {
        return VND;
    }

    public void setVND(double VND) {
        this.VND = VND;
    }
}

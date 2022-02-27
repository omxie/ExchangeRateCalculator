package com.example.exchangeratecalculator;

import java.util.HashMap;
import java.util.Map;

public class CurrencyRatesData {

    private String base, date;
    private long timestamp;
    private boolean success;
    private Map<String, Double> rates;

    public CurrencyRatesData(String base, String date, boolean success, Map<String, Double> rates) {
        this.base = base;
        this.date = date;
        this.success = success;
        this.rates = rates;
    }

    public boolean getSuccess() { return success; }
    public void setSuccess(boolean value) { this.success = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long value) { this.timestamp = value; }

    public String getBase() { return base; }
    public void setBase(String value) { this.base = value; }

    public String getDate() { return date; }
    public void setDate(String value) { this.date = value; }

    public Map<String, Double> getRates() { return rates; }
    public void setRates(Map<String, Double> value) { this.rates = value; }

}

package com.example.exchangeratecalculator;

public class CurrencyData {

    private int CurrencyIcon;
    private String CurrencyTitle, CurrencyName;
    private double CurrencyRate;

    public CurrencyData(int currencyIcon, String currencyTitle, String currencyName, double currencyRate) {
        CurrencyIcon = currencyIcon;
        CurrencyTitle = currencyTitle;
        CurrencyName = currencyName;
        CurrencyRate = currencyRate;
    }

    public int getCurrencyIcon() {
        return CurrencyIcon;
    }

    public void setCurrencyIcon(int currencyIcon) {
        CurrencyIcon = currencyIcon;
    }

    public String getCurrencyTitle() {
        return CurrencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        CurrencyTitle = currencyTitle;
    }

    public String getCurrencyName() {
        return CurrencyName;
    }

    public void setCurrencyName(String currencyName) {
        CurrencyName = currencyName;
    }

    public double getCurrencyRate() {
        return CurrencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        CurrencyRate = currencyRate;
    }
}

package com.example.exchangeratecalculator;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;

import static android.content.ContentValues.TAG;

public class HttpConnect implements Callable<Boolean> {

    private Context context;
    public static CurrencyRatesData currencyData;

    public HttpConnect (Context context) {
        this.context = context;
    }


    @Override
    public Boolean call() throws Exception {

        try {
            Log.d(TAG, "call: INSIDE CALLABLE");
            URL url = new URL("http://api.exchangeratesapi.io/v1/latest?access_key=ebd9837cb2a457b7305b47f64da2027c"); //Enter URL here

            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("GET"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are receiving which is `application/json`

            httpURLConnection.connect();
            InputStream response = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response));

            Gson gson = new Gson();
            currencyData =  gson.fromJson(reader, CurrencyRatesData.class);

            Log.d(TAG, "call: "+currencyData.getRates());
        } catch (Exception e) {
            Log.d(TAG, "call: Exception Caught: "+e);
        }
        return false;
    }
}

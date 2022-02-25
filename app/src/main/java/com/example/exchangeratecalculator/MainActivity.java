package com.example.exchangeratecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    int[] CurrencyIconList = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background};

    String[] CurrencyNameList = {"EURO", "Australian Dollar", "Indian Rupee"};

    String[] CurrencyTitleList = {"EUR", "AUD", "INR"};

    Double[] CurrencyRateList = {1.23, 1.22, 1.55, 1.55};

    RecyclerView CurrencyRecycler;
    CurrencyDataAdapter CurrencyDataAdapter;
    List<CurrencyData> CurrencyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //puts data into a list of type CurrencyData
        addDataToRecyclerList();

        CurrencyRecycler = findViewById(R.id.CurrencyRecycler);
        CurrencyDataAdapter = new CurrencyDataAdapter(CurrencyDataList, MainActivity.this);
        RecyclerView.LayoutManager CurrencyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CurrencyRecycler.setLayoutManager(CurrencyLayoutManager);
        CurrencyRecycler.setItemAnimator(new DefaultItemAnimator());
        CurrencyRecycler.setAdapter(CurrencyDataAdapter);
    }

    public void addDataToRecyclerList(){
        for (int i = 0; i<CurrencyIconList.length; i++){

            String Name = CurrencyNameList[i];
            int Icon = CurrencyIconList[i];
            String Title = CurrencyTitleList[i];
            double Rate = CurrencyRateList[i];

            CurrencyData CurrencyData = new CurrencyData(Icon, Title, Name, Rate);
            CurrencyDataList.add(CurrencyData);
        }
    }

}
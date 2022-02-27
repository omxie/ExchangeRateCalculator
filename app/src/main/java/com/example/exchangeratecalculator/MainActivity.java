package com.example.exchangeratecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    int[] CurrencyIconList = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background};
    List<Integer> IconList = new ArrayList<Integer>();
    String[] CurrencyNameList = {"EURO", "Australian Dollar", "Indian Rupee"};

    String[] CurrencyTitleList = {"EUR", "AUD", "INR"};

    Double[] CurrencyRateList = {1.23, 1.22, 1.55, 1.55};

    private List<Callable<Boolean>> TaskList;

    RecyclerView CurrencyRecycler;
    CurrencyDataAdapter CurrencyDataAdapter;
    List<CurrencyData> CurrencyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskList = new ArrayList<>(50);
        TypedArray list = getResources().obtainTypedArray(R.array.flag_ids);
        for (int i = 0; i < list.length(); ++i) {
            int id = list.getResourceId(i, -1);
            IconList.add(id);
        }


        try {
            getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        CurrencyTitleList = HttpConnect.currencyData.getRates().keySet().toArray(new String[0]);
        CurrencyRateList = HttpConnect.currencyData.getRates().values().toArray(new Double[0]);

        for (int i = 0; i< IconList.size(); i++){

            String Name = "Currency Data";
            int Icon = IconList.get(i);
            String Title = CurrencyTitleList[i];
            double Rate = CurrencyRateList[i];

            CurrencyData CurrencyData = new CurrencyData(Icon, Title, Name, Rate);
            CurrencyDataList.add(CurrencyData);
        }
    }

    public void getData() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(30);

        TaskList.add(new HttpConnect(this));

        List<Future<Boolean>> Futures = executorService.invokeAll(TaskList);

        if(Futures.get(0).isDone())
            Log.d(TAG, "getData: Currency Values were Fetched successfully");

        TaskList.clear();


    }

}
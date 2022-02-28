package com.example.exchangeratecalculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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

    Double[] FetchedRateList;
    Double[] CurrencyRateList = {1.23, 1.22, 1.55, 1.55};

    private List<Callable<Boolean>> TaskList;

    public boolean ValuesUpdated = false;
    private EditText EuroRate;
    public RecyclerView CurrencyRecycler;
    public CurrencyDataAdapter CurrencyDataAdapter;
    public List<CurrencyData> CurrencyDataList = new ArrayList<>();

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

        EuroRate = (EditText) findViewById(R.id.EuroRate);

        //set the default value of EUR to 1.0
        EuroRate.setText("1.0");

        EuroRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()){
                    FetchedRateList = CurrencyRateList = HttpConnect.currencyData.getRates().values().toArray(new Double[0]);
                    double euroRate = Double.parseDouble(editable.toString());
                    for(int i = 0; i < FetchedRateList.length; i++) {
                        CurrencyRateList[i] = FetchedRateList[i] * euroRate;
                        ValuesUpdated = true;
                    }
                    addDataToRecyclerList();
                }

            }
        });

        try {
            getData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        CurrencyRecycler = findViewById(R.id.CurrencyRecycler);
        CurrencyDataAdapter = new CurrencyDataAdapter(CurrencyDataList, MainActivity.this);
        RecyclerView.LayoutManager CurrencyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        CurrencyRecycler.setLayoutManager(CurrencyLayoutManager);
        CurrencyRecycler.setItemAnimator(new DefaultItemAnimator());
        CurrencyRecycler.setAdapter(CurrencyDataAdapter);

        //puts data into a list of type CurrencyData
        addDataToRecyclerList();

    }

    public void addDataToRecyclerList(){
        CurrencyTitleList = HttpConnect.currencyData.getRates().keySet().toArray(new String[0]);

        if (!ValuesUpdated)
            CurrencyRateList = HttpConnect.currencyData.getRates().values().toArray(new Double[0]);

        //clear the data list before we add values to it
        CurrencyDataList.clear();

        //map data collected and pass it to the Currency Data class
        for (int i = 0; i< IconList.size(); i++){

            String Name = "Currency Data";
            int Icon = IconList.get(i);
            String Title = CurrencyTitleList[i];
            double Rate = CurrencyRateList[i];

            CurrencyData CurrencyData = new CurrencyData(Icon, Title, Name, Rate);
            CurrencyDataList.add(CurrencyData);
            Log.d(TAG, "addDataToRecyclerList: "+CurrencyDataList.get(i).getCurrencyRate());
        }
        //CurrencyDataAdapter = new CurrencyDataAdapter(CurrencyDataList, MainActivity.this);
       // CurrencyRecycler.setAdapter(CurrencyDataAdapter);
        CurrencyDataAdapter.notifyDataSetChanged();
        CurrencyRecycler.setAdapter(CurrencyDataAdapter);
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
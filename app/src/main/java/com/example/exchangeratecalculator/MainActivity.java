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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    //int[] CurrencyIconList = {R.drawable.ic_launcher_foreground, R.drawable.ic_launcher_background};
    List<Integer> iconList = new ArrayList<Integer>();
    //String[] CurrencyNameList = {"EURO", "Australian Dollar", "Indian Rupee"};

    String[] currencyTitleList = {"EUR", "AUD", "INR"};

    Double[] FetchedRateList;
    Double[] currencyRateList = {1.23, 1.22, 1.55, 1.55};

    private List<Callable<Boolean>> taskList;

    public static boolean computingRecyclerView = false;
    public static boolean valuesUpdated = false;
    private EditText euroRate;
    public RecyclerView currencyRecycler;
    public CurrencyDataAdapter currencyDataAdapter;
    public List<CurrencyData> currencyDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskList = new ArrayList<>(50);

        //Fetches flags from the values.xml and puts it into an array
        TypedArray list = getResources().obtainTypedArray(R.array.flag_ids);
        for (int i = 0; i < list.length(); ++i) {
            int id = list.getResourceId(i, -1);
            iconList.add(id);
        }

        euroRate = (EditText) findViewById(R.id.EuroRate);

        //set the default value of EUR to 1.0
        euroRate.setText("1.0");

        euroRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!editable.toString().isEmpty()){
                    FetchedRateList = currencyRateList = HttpConnect.currencyData.getRates().values().toArray(new Double[0]);
                    double euroRate = Double.parseDouble(editable.toString());
                    for(int i = 0; i < FetchedRateList.length; i++) {
                        currencyRateList[i] = FetchedRateList[i] * euroRate;
                        valuesUpdated = true;
                    }
                    addDataToRecyclerList();
                }

            }
        });

        //This sends an http request every 1 second, basically refreshing the data
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "run: INSIDE THE TIMER");
                    getData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 1000);



        currencyRecycler = findViewById(R.id.CurrencyRecycler);
        currencyDataAdapter = new CurrencyDataAdapter(currencyDataList, MainActivity.this);
        RecyclerView.LayoutManager CurrencyLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        currencyRecycler.setLayoutManager(CurrencyLayoutManager);
        currencyRecycler.setItemAnimator(new DefaultItemAnimator());
        currencyRecycler.setAdapter(currencyDataAdapter);

        //delay is added here to avoid a null pointer
        //caused by the addDataToRecyclerList() method
        //some of the fetched data wasn't fetched during the time the method was invoked.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //puts data into a list of type CurrencyData
        addDataToRecyclerList();

        if (!currencyRecycler.isComputingLayout())
            computingRecyclerView = true;


    }

    //This function adds data to the recycler view and also updates the rates
    public void addDataToRecyclerList(){
        currencyTitleList = HttpConnect.currencyData.getRates().keySet().toArray(new String[0]);

        if (!valuesUpdated)
            currencyRateList = HttpConnect.currencyData.getRates().values().toArray(new Double[0]);

        //clear the data list before we add values to it
        currencyDataList.clear();

        //map data collected and pass it to the Currency Data class
        for (int i = 0; i< iconList.size(); i++){

            String Name = "Currency Data";
            int Icon = iconList.get(i);
            String Title = currencyTitleList[i];
            double Rate = currencyRateList[i];

            CurrencyData CurrencyData = new CurrencyData(Icon, Title, Name, Rate);
            currencyDataList.add(CurrencyData);
            Log.d(TAG, "addDataToRecyclerList: "+currencyDataList.get(i).getCurrencyRate());
        }
        //currencyDataAdapter = new CurrencyDataAdapter(currencyDataList, MainActivity.this);
       // CurrencyRecycler.setAdapter(currencyDataAdapter);
        currencyDataAdapter.notifyDataSetChanged();
        currencyRecycler.setAdapter(currencyDataAdapter);
    }

    public void getData() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(30);

        taskList.add(new HttpConnect(this));

        List<Future<Boolean>> Futures = executorService.invokeAll(taskList);

        //clear the task list once http get request is completed.
        if(Futures.get(0).isDone()) {
            taskList.clear();

            //these two lines right here is what updates the view with the new values fetched
            if (!MainActivity.computingRecyclerView){
                valuesUpdated = false;
                addDataToRecyclerList();
            }

            Log.d(TAG, "getData: Currency Values were Fetched successfully");
        }

    }

}
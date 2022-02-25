package com.example.exchangeratecalculator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CurrencyDataAdapter extends RecyclerView.Adapter<CurrencyDataAdapter.CurrencyDataViewHolder> {

    private List<CurrencyData> CurrencyDataList;
    private Context context;

    public CurrencyDataAdapter(List<CurrencyData> currencyDataList, Context context) {
        CurrencyDataList = currencyDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrencyDataAdapter.CurrencyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View ItemView = LayoutInflater.from(context).inflate(R.layout.layout_currency_data_cell, parent, false);
        return new CurrencyDataAdapter.CurrencyDataViewHolder(ItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyDataViewHolder holder, int position) {
        holder.CurrencyIcon.setImageResource(CurrencyDataList.get(position).getCurrencyIcon());
        holder.CurrencyTitle.setText(CurrencyDataList.get(position).getCurrencyTitle());
        holder.CurrencyName.setText(CurrencyDataList.get(position).getCurrencyName());
       // holder.CurrencyRate.setText(CurrencyDataList.get(position).getCurrencyRate());

    }


    @Override
    public int getItemCount() {
        return CurrencyDataList.size();
    }

    public class CurrencyDataViewHolder extends RecyclerView.ViewHolder {

        ImageView CurrencyIcon;
        TextView CurrencyName, CurrencyTitle;
        EditText CurrencyRate;

        public CurrencyDataViewHolder(View view) {
            super(view);
            CurrencyIcon = view.findViewById(R.id.CurrencyIcon);
            CurrencyName = view.findViewById(R.id.CurrencyName);
            CurrencyTitle = view.findViewById(R.id.CurrencyTitle);
            CurrencyRate = view.findViewById(R.id.CurrencyRate);
        }

    }
}

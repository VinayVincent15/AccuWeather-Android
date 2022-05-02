package com.example.accuweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder>{
    private Context context;
    private ArrayList<WeatherRVModal> weatherRVModalArrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherRVModal> weatherRVModalArrayList) {
        this.context = context;
        this.weatherRVModalArrayList = weatherRVModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherRVModal modal = weatherRVModalArrayList.get(position);
        holder.temperatureTV.setText(String.format("%s°c", modal.getTemp()));
        Picasso.get().load("http:".concat(modal.getIcon())).into(holder.conditionIV);

    }

    @Override
    public int getItemCount() {
        return weatherRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView temperatureTV;                                   //dayTV,
        private ImageView conditionIV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //dayTV=itemView.findViewById(R.id.idTVDay);
            temperatureTV=itemView.findViewById(R.id.idTVTemperature);
            conditionIV=itemView.findViewById(R.id.idTVCondition);

        }
    }
}

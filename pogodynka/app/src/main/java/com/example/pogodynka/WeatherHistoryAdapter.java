package com.example.pogodynka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<String> weather_cityName, weather_temperature, weather_pressure, weather_humidity, weather_time;

    WeatherHistoryAdapter(Context context,
                          ArrayList weather_cityName,
                          ArrayList weather_temperature,
                          ArrayList weather_pressure,
                          ArrayList weather_humidity,
                          ArrayList weather_time) {
        this.weather_cityName = weather_cityName;
        this.weather_temperature = weather_temperature;
        this.weather_pressure = weather_pressure;
        this.weather_humidity = weather_humidity;
        this.weather_time = weather_time;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.cityName.setText(weather_cityName.get(position));
        holder.temperature.setText(weather_temperature.get(position));
        holder.pressure.setText(weather_pressure.get(position));
        holder.humidity.setText(weather_humidity.get(position));
        holder.time.setText(weather_time.get(position));
    }

    @Override
    public int getItemCount() {
        return weather_cityName.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView cityName, temperature, pressure, humidity, time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.weather_cityName);
            temperature = itemView.findViewById(R.id.weather_temperature);
            pressure = itemView.findViewById(R.id.weather_pressure);
            humidity = itemView.findViewById(R.id.weather_humidity);
            time = itemView.findViewById(R.id.weather_time);
        }
    }
}

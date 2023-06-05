package com.example.pogodynka;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MyDatabaseHelper myDB;
    ArrayList<String> weather_cityName, weather_temperature, weather_pressure, weather_humidity, weather_time;
    WeatherHistoryAdapter weatherHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerView);

        myDB = new MyDatabaseHelper(HistoryActivity.this);

        weather_cityName = new ArrayList<String>();
        weather_temperature = new ArrayList<String>();
        weather_pressure = new ArrayList<String>();
        weather_humidity = new ArrayList<String>();
        weather_time = new ArrayList<String>();

        storeDataInArrays();

        weatherHistoryAdapter = new WeatherHistoryAdapter(
                HistoryActivity.this,
                weather_cityName,
                weather_temperature,
                weather_pressure,
                weather_humidity,
                weather_time);
        recyclerView.setAdapter(weatherHistoryAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
    }

    public void storeDataInArrays() {
        Cursor cursor = myDB.readAllData();
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
            return;
        }
        while (cursor.moveToNext()) {
            weather_cityName.add(cursor.getString(1));
            weather_temperature.add(cursor.getString(2));
            weather_pressure.add(cursor.getString(3));
            weather_humidity.add(cursor.getString(4));
            weather_time.add(cursor.getString(5));
        }

    }
}
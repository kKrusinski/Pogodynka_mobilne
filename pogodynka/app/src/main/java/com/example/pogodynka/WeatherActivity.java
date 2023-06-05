package com.example.pogodynka;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherActivity extends AppCompatActivity {
    TextView cityName, temp, pressure, humidity, tempMin, tempMax, curTime;
    ImageView weatherImage;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);
        cityName = findViewById(R.id.cityName);
        temp = findViewById(R.id.temp);
        pressure = findViewById(R.id.press);
        humidity = findViewById(R.id.humi);
        tempMin = findViewById(R.id.tempMin);
        tempMax = findViewById(R.id.tempMax);
        curTime = findViewById(R.id.currentTime);
        weatherImage = findViewById(R.id.imageView);

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
            }
        };
        timer.schedule(doTask, 300000);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        checkTheWeather();
                    }
                }
        );

        checkTheWeather();

    }



    public void checkTheWeather(){
        Intent intent = getIntent();
        String text = intent.getStringExtra("addedCity");
        cityName.setText(text);


        String url = "https://api.openweathermap.org/data/2.5/weather?q=";
        url += text;
        url += ",pl&units=metric&APPID=749561a315b14523a8f5f1ef95e45864";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject mainObject = response.getJSONObject("main");
                    String tempr = String.valueOf(mainObject.getDouble("temp"));
                    String pressu = String.valueOf(mainObject.getInt("pressure"));
                    String humidi = String.valueOf(mainObject.getInt("humidity"));
                    String minTmp = String.valueOf(mainObject.getDouble("temp_min"));
                    String maxTmp = String.valueOf(mainObject.getDouble("temp_max"));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.ENGLISH);
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                    curTime.setText(simpleDateFormat.format(new Date()));

                    temp.setText(tempr + " °C");
                    pressure.setText(pressu + " hPa");
                    humidity.setText(humidi + " %");
                    tempMin.setText(minTmp + " °C");
                    tempMax.setText(maxTmp + " °C");

                    saveWeatherToHistory();

                    JSONArray picObj = response.getJSONArray("weather");
                    JSONObject nudes = picObj.getJSONObject(0);

                    String imIc = nudes.getString("icon");
                    String imgUrl = "https://openweathermap.org/img/wn/";
                    imgUrl += imIc;
                    imgUrl += "@2x.png";

                    Picasso.get().load(imgUrl).into(weatherImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                curTime.setText("Error");
                temp.setText("Error");
                pressure.setText("Error");
                humidity.setText("Error");
                tempMin.setText("Error");
                tempMax.setText("Error");
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
        mSwipeRefreshLayout.setRefreshing(false);
    }
    public void goBack(View view) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(WeatherActivity.this, MainActivity.class);
                    startActivity(intent);
            }
        });
    }

    private void saveWeatherToHistory() {
        String name = cityName.getText().toString();
        String temperature = temp.getText().toString();
        String pres = pressure.getText().toString();
        String hum = humidity.getText().toString();
        String time = curTime.getText().toString();

        MyDatabaseHelper myDB = new MyDatabaseHelper(WeatherActivity.this);
        myDB.addWeatherInfo(name, temperature, pres, hum, time);
    }

}
package com.example.pogodynka;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private int INTERNET_PERMISSION_CODE = 1;
    private int ACCESS_NETWORK_STATE_PERMISSION_CODE = 2;

    Button checkWeather;
    EditText cityName;

    Button historyButton;

    String text;

    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkWeather = findViewById(R.id.checkWeather);
        cityName = findViewById(R.id.editCityName);
        historyButton = findViewById(R.id.historyButton);

        checkPermissions();
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {
            requestInternetPermission();
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED) {
            requestAccessNetworkStatePermission();
        } else {
            Toast.makeText(this, "all permissions granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestInternetPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.INTERNET }, INTERNET_PERMISSION_CODE);
        }
    }

    private void requestAccessNetworkStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.ACCESS_NETWORK_STATE}, ACCESS_NETWORK_STATE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_NETWORK_STATE }, ACCESS_NETWORK_STATE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == ACCESS_NETWORK_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager objConnectivityManager;
        try {
            objConnectivityManager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            final boolean IsWifiAvailable = objConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();

            boolean IsInternetAvailable = false;
            if (objConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null)
                IsInternetAvailable = objConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
            if (IsWifiAvailable == true || IsInternetAvailable == true)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void checkWeatherHere(View view) {
        final boolean isNetworkAvailable = isNetworkAvailable(this);
        final Toast mess = Toast.makeText(this, "Internet connection failed", Toast.LENGTH_SHORT);
        if (isNetworkAvailable) {
            String thisCityName = cityName.getText().toString();
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            intent.putExtra("addedCity", thisCityName);
            startActivity(intent);
        } else {
            mess.show();
        }
    }

    public void openAboutPage(View view) {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public void openOptionsPage(View view) {
        Intent intent = new Intent(MainActivity.this, OptionsActivity.class);
        startActivity(intent);
    }

    public void openFavoriteLocation(View view) {
        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        text = sp.getString("favorite_location", "");
        if (text.isEmpty()) {
            Toast.makeText(this, "no favorite location", Toast.LENGTH_SHORT).show();
            return;
        }
        cityName.setText(text);
        checkWeatherHere(view);
    }

    public void openHistoryPage(View view) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }
}
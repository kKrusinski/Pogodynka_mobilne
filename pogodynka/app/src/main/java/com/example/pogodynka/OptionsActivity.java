package com.example.pogodynka;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    Button button;
    EditText editText;

    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.editField);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        text = sp.getString("favorite_location", "");
        editText.setText(text);
    }

    public void save(View view) {
        String text = editText.getText().toString();

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("favorite_location", text);
        editor.apply();
    }
}
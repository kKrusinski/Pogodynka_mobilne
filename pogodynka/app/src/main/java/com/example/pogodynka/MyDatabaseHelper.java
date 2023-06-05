package com.example.pogodynka;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "WeatherHistory.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "my_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CITY_NAME = "cityName";
    private static final String COLUMN_TEMP = "temperature";
    private static final String COLUMN_PRESSURE = "pressure";
    private static final String COLUMN_HUMIDITY = "humidity";
    private static final String COLUMN_TIME = "time";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITY_NAME + " TEXT," +
                COLUMN_TEMP + " TEXT," +
                COLUMN_PRESSURE + " TEXT," +
                COLUMN_HUMIDITY + " TEXT," +
                COLUMN_TIME + " TEXT" +
                ");";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    void addWeatherInfo(String name, String temperature, String pres, String hum, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_CITY_NAME, name);
        cv.put(COLUMN_TEMP, temperature);
        cv.put(COLUMN_PRESSURE, pres);
        cv.put(COLUMN_HUMIDITY, hum);
        cv.put(COLUMN_TIME, time);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Saved to history", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}

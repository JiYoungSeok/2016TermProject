package jiyoungseok.mylifelogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TaskDBManager extends SQLiteOpenHelper{
    public TaskDBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(int date, String category, Double latitude, Double longitude, int time) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("SQL", "Date : " + date + " category : " + category + " latitude : " + latitude + " longitude : " + longitude + " time + " + time);
        db.execSQL("INSERT INTO database VALUES (NULL, " + date + ", '" + category + "', " + latitude + ", " + longitude + ", " + time + ");");
        db.close();
    }

    public int getTime(int getDate, String getCategory) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        Log.d("SQL", "Date : " + getDate + "Category : " + getCategory);
        int todayTotalTime = 0;

        while (cursor.moveToNext()) {
            int date = cursor.getInt(cursor.getColumnIndex("date"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));

            if ((getDate == date) && (category.equals(getCategory)))
            {
                todayTotalTime = todayTotalTime + time;
            }
        }
        return todayTotalTime;
    }

    public void showMarker (ArrayList<LogList> al) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        while (cursor.moveToNext()) {
            int todayDate = cursor.getInt(cursor.getColumnIndex("date"));
            int currentTime = cursor.getInt(cursor.getColumnIndex("time"));
            Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String event = cursor.getString(cursor.getColumnIndex("category"));

            LogList logList = new LogList();

            logList.setTodayDate(todayDate);
            logList.setCurrentTime(currentTime);
            logList.setLatitude(latitude);
            logList.setLongitude(longitude);
            logList.setEvent(event);
            logList.setMemo("");

            al.add(logList);
        }
    }
}

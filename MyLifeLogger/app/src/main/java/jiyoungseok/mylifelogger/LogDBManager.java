package jiyoungseok.mylifelogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class LogDBManager extends SQLiteOpenHelper {

    public LogDBManager (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, todayDate INTEGER , currentTime INTEGER, latitude REAL, longitude REAL, event TEXT, memo TEXT);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert (int todayDate, int currentTime, Double latitude, Double longitude, String event, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("SQL", "TodayDate : " + todayDate + " CurrentTime : " + currentTime + " Latitude : " + latitude + " Longitude : " + longitude + " Event : " + event + " Memo : " + memo);
        db.execSQL("INSERT INTO database VALUES (NULL, " + todayDate + ", " + currentTime + ", " + latitude + ", " + longitude + ", '" + event + "', '" + memo + "');");
    }

    public void showMarker (ArrayList<LogList> al) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        while (cursor.moveToNext()) {
            int todayDate = cursor.getInt(cursor.getColumnIndex("todayDate"));
            int currentTime = cursor.getInt(cursor.getColumnIndex("currentTime"));
            Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String event = cursor.getString(cursor.getColumnIndex("event"));
            String memo = cursor.getString(cursor.getColumnIndex("memo"));

            LogList logList = new LogList();

            logList.setTodayDate(todayDate);
            logList.setCurrentTime(currentTime);
            logList.setLatitude(latitude);
            logList.setLongitude(longitude);
            logList.setEvent(event);
            logList.setMemo(memo);

            al.add(logList);
        }
    }
}

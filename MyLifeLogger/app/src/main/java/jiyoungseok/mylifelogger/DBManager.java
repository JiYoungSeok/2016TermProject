package jiyoungseok.mylifelogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBManager  extends SQLiteOpenHelper {

    public DBManager (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, type INTEGER, date INTEGER , time INTEGER, latitude REAL, longitude REAL, event TEXT, memo TEXT);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert (int type, int date, int time, Double latitude, Double longitude, String event, String memo) {
        SQLiteDatabase db = getWritableDatabase();
        Log.d("SQL", "Type : " + type + "Date : " + date + " Time : " + time + " Latitude : " + latitude + " Longitude : " + longitude + " Event : " + event + " Memo : " + memo);
        db.execSQL("INSERT INTO database VALUES (NULL, " + type + ", " + date + ", " + time + ", " + latitude + ", " + longitude + ", '" + event + "', '" + memo + "');");
    }

    public int getTime(int getDate, String getEvent) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        Log.d("SQL", "Date : " + getDate + "Event : " + getEvent);
        int todayTotalTime = 0;

        while (cursor.moveToNext()) {
            int date = cursor.getInt(cursor.getColumnIndex("date"));
            String event = cursor.getString(cursor.getColumnIndex("event"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));

            if ((getDate == date) && (event.equals(getEvent)))
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
            int type = cursor.getInt(cursor.getColumnIndex("type"));
            int date = cursor.getInt(cursor.getColumnIndex("date"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));
            Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
            Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
            String event = cursor.getString(cursor.getColumnIndex("event"));
            String memo = cursor.getString(cursor.getColumnIndex("memo"));

            LogList logList = new LogList();

            logList.setType(type);
            logList.setDate(date);
            logList.setTime(time);
            logList.setLatitude(latitude);
            logList.setLongitude(longitude);
            logList.setEvent(event);
            logList.setMemo(memo);

            al.add(logList);
        }
    }
}

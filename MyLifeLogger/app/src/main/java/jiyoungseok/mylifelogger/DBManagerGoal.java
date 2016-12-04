package jiyoungseok.mylifelogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManagerGoal extends SQLiteOpenHelper {

    public DBManagerGoal (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super (context,name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("CREATE TABLE goal (_id INTEGER PRIMARY KEY AUTOINCREMENT, startDate INTEGER, endDate INTEGER, time INTEGER, category TEXT, upOrDown TEXT);");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVerision) {
    }

    public void insert (int startDate, int endDate, int time, String category, String upOrDown) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO goal VALUES (NULL, " + startDate + ", " + endDate + ", " + time + ", '" + category + "', '" + upOrDown + "');");
        db.close();
    }

    public void delete (int startDate, int endDate, int time, String category, String upOrDown) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM goal WHERE startDate = " + startDate + " AND endDate = " + endDate + " AND time = " + time + " AND category = '" + category + "' AND upOrDown = '" + upOrDown + "'");
        db.close();
    }

    public void showList (ArrayList<GoalList> al) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM goal", null);

        while (cursor.moveToNext()) {
            int startDate = cursor.getInt(cursor.getColumnIndex("startDate"));
            int endDate = cursor.getInt(cursor.getColumnIndex("endDate"));
            int time = cursor.getInt(cursor.getColumnIndex("time"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String upOrDown = cursor.getString(cursor.getColumnIndex("upOrDown"));

            GoalList goalList= new GoalList();

            goalList.setStartDate(startDate);
            goalList.setEndDate(endDate);
            goalList.setTime(time);
            goalList.setCategory(category);
            goalList.setUpOrDown(upOrDown);

            al.add(goalList);
        }
    }
}

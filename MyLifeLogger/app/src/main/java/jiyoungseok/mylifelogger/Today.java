package jiyoungseok.mylifelogger;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Today {
    private int year, month, day;
    private GregorianCalendar calendar = new GregorianCalendar();

    Today () {
        this.year = calendar.get(Calendar.YEAR);
        this.month = (calendar.get(Calendar.MONTH) + 1);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setYear(int year) { this.year = year; }
    public void setMonth(int month) { this.month = month; }
    public void setDay(int day) { this.day = day; }

    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
}

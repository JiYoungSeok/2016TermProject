package jiyoungseok.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonReset;
    TextView textViewTodayDate;

    private long lastTimeBackPressed;
    private long timeWhenStopped = 0;

    Today today = new Today();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        buttonStart = (Button) findViewById(R.id.button_Start);
        buttonStop = (Button) findViewById(R.id.button_Stop);
        buttonReset = (Button) findViewById(R.id.button_Reset);

        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewTodayDate.setText(today.getYear() + "년 " + today.getMonth() + "월 " + today.getDay() + "일");

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);

        buttonStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                chronometer.start();
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
            }
        });

        buttonReset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;
            }
        });
    }

    public void onClickCalendar(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;
                textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
                textViewTodayDate.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");

                today.setYear(year);
                today.setMonth(monthOfYear - 1);
                today.setDay(dayOfMonth);
            }
        };
        new DatePickerDialog(TaskActivity.this, dateSetListener, today.getYear(), today.getMonth(), today.getDay()).show();
    }

    public void onClickStart(View view) {
        TextView textViewWhatToDo = (TextView) findViewById(R.id.textView_WhatToDo);
        switch(view.getId()) {
            case R.id.startStudy:
                textViewWhatToDo.setText("Study");
                break;
            case R.id.startWork:
                textViewWhatToDo.setText("Work");
                break;
            case R.id.startHobby:
                textViewWhatToDo.setText("Hobby");
                break;
            case R.id.startWorkout:
                textViewWhatToDo.setText("Workout");
                break;
            case R.id.startSleep:
                textViewWhatToDo.setText("Sleep");
                break;
            case R.id.startDate:
                textViewWhatToDo.setText("Date");
                break;
            case R.id.startEat:
                textViewWhatToDo.setText("Eat");
                break;
            case R.id.startMove:
                textViewWhatToDo.setText("Move");
                break;
            case R.id.startOther:
                textViewWhatToDo.setText("Other");
                break;
        }
    }

    public void onClickChangePage(View view) {
        switch(view.getId()) {
            case R.id.button_Task:
                Intent moveToTask = new Intent (getApplicationContext(), TaskActivity.class);
                startActivity(moveToTask);
                break;
            case R.id.button_Log:
                Intent moveToLog = new Intent (getApplicationContext(), LogActivity.class);
                startActivity(moveToLog);
                break;
            case R.id.button_Map:
                Intent moveToMap = new Intent (getApplicationContext(), MapActivity.class);
                startActivity(moveToMap);
                break;
            case R.id.button_Goal:
                Intent moveToGoal = new Intent (getApplicationContext(), GoalActivity.class);
                startActivity(moveToGoal);
                break;
        }
    }

    public void onBackPressed() {
        if((System.currentTimeMillis() - lastTimeBackPressed) < 1500) {
            finish();
            return;
        }
        Toast.makeText(TaskActivity.this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
}

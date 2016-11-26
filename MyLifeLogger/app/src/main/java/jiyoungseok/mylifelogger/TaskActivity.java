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

    Button buttonStart, buttonStop, buttonReset, buttonSave;
    TextView textViewTodayDate, textViewStudy;
    Chronometer chronometer;

    private long lastTimeBackPressed;
    private long timeWhenStopped = 0;
    private long currentTime;
    private int convertTime;
    private boolean isTimerRun = false;

    Today today = new Today();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        buttonStart = (Button) findViewById(R.id.button_Start);
        buttonStop = (Button) findViewById(R.id.button_Stop);
        buttonReset = (Button) findViewById(R.id.button_Reset);
        buttonSave = (Button) findViewById(R.id.button_Save);

        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewStudy = (TextView) findViewById(R.id.textView_Study);
        textViewTodayDate.setText(today.getYear() + "년 " + today.getMonth() + "월 " + today.getDay() + "일");

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        buttonStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                chronometer.start();
                isTimerRun = true;
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                timeWhenStopped = chronometer.getBase() - SystemClock.elapsedRealtime();
                isTimerRun = false;
            }
        });

        buttonReset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                timeWhenStopped = 0;
                chronometer.stop();
                isTimerRun = false;
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                convertTime = (int) currentTime / 1000;

                textViewStudy.setText(chronometer.getText());
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
            }
        };
        new DatePickerDialog(TaskActivity.this, dateSetListener, today.getYear(), today.getMonth(), today.getDay()).show();
    }

    public void onClickStart(View view) {
        TextView textViewWhatToDo = (TextView) findViewById(R.id.textView_WhatToDo);
        switch(view.getId()) {
            case R.id.startStudy:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("공부");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWork:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("직장");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startHobby:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("취미");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWorkout:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("운동");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startDate:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("데이트");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startMove:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("이동");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startOther:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("기타");
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
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
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, buttonReset, buttonSave;
    TextView textViewTodayDate, textViewWhatToDo;
    TextView textViewStudy, textViewWork, textViewHobby, textViewWorkout, textViewDate, textViewMove, textViewOther;
    LinearLayout startStudy, startWork, startHobby, startWorkout, startDate, startMove, startOther;
    Chronometer chronometer;

    final TaskDBManager dbManager = new TaskDBManager(this, "task.db", null, 1);
    final int SECONDS_PER_MINUTE = 60;
    final int SECONDS_PER_HOUR = 3600;
    final int YEAR_TO_CONVERTDATE = 10000;
    final int MONTH_TO_CONVERTDATE = 100;

    private int currentTime;
    private int currentTimeToSeconds;
    private int convertDate;
    private int switchWhatToDo;
    private long lastTimeBackPressed;
    private long timeWhenStopped = 0;
    private boolean isTimerRun = false;

    Today today = new Today();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        setText();

        buttonStart = (Button) findViewById(R.id.button_Start);
        buttonStop = (Button) findViewById(R.id.button_Stop);
        buttonReset = (Button) findViewById(R.id.button_Reset);
        buttonSave = (Button) findViewById(R.id.button_Save);

        textViewWhatToDo = (TextView) findViewById(R.id.textView_WhatToDo);
        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewTodayDate.setText(today.getYear() + "년 " + today.getMonth() + "월 " + today.getDay() + "일");
        convertDate = today.getYear() * YEAR_TO_CONVERTDATE + today.getMonth() * MONTH_TO_CONVERTDATE + today.getDay();

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
                currentTime = Integer.parseInt(chronometer.getText().toString().substring(0,2) + chronometer.getText().toString().substring(3));
                currentTimeToSeconds = ((currentTime / 100) * SECONDS_PER_MINUTE) + (currentTime % 100);

                if (isTimerRun == false) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeWhenStopped = 0;
                    chronometer.stop();

                    switch (switchWhatToDo) {
                        case 1:
                            dbManager.insert(convertDate, "공부", currentTimeToSeconds);
                            setText();
                            break;
                        case 2:
                            dbManager.insert(convertDate, "직장", currentTimeToSeconds);
                            setText();
                            break;
                        case 3:
                            dbManager.insert(convertDate, "취미", currentTimeToSeconds);
                            setText();
                            break;
                        case 4:
                            dbManager.insert(convertDate, "운동", currentTimeToSeconds);
                            setText();
                            break;
                        case 5:
                            dbManager.insert(convertDate, "데이트", currentTimeToSeconds);
                            setText();
                            break;
                        case 6:
                            dbManager.insert(convertDate, "이동", currentTimeToSeconds);
                            setText();
                            break;
                        case 7:
                            dbManager.insert(convertDate, "기타", currentTimeToSeconds);
                            setText();
                            break;
                    }
                } else {
                    Toast.makeText(TaskActivity.this, "타이머를 멈춘 뒤 저장하세요.", Toast.LENGTH_SHORT).show();
                }
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
        textViewWhatToDo = (TextView) findViewById(R.id.textView_WhatToDo);
        startStudy = (LinearLayout) findViewById(R.id.startStudy);
        startWork = (LinearLayout) findViewById(R.id.startWork);
        startHobby = (LinearLayout) findViewById(R.id.startHobby);
        startWorkout = (LinearLayout) findViewById(R.id.startWorkout);
        startDate = (LinearLayout) findViewById(R.id.startDate);
        startMove = (LinearLayout) findViewById(R.id.startMove);
        startOther = (LinearLayout) findViewById(R.id.startOther);

        switch(view.getId()) {
            case R.id.startStudy:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("공부");
                    switchWhatToDo = 1;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWork:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("직장");
                    switchWhatToDo = 2;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startHobby:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("취미");
                    switchWhatToDo = 3;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWorkout:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("운동");
                    switchWhatToDo = 4;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startDate:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("데이트");
                    switchWhatToDo = 5;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startMove:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("이동");
                    switchWhatToDo = 6;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startOther:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("기타");
                    switchWhatToDo = 7;
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

    public void setText() {
        textViewStudy = (TextView) findViewById(R.id.textView_Study);
        textViewWork = (TextView) findViewById(R.id.textView_Work);
        textViewHobby = (TextView) findViewById(R.id.textView_Hobby);
        textViewWorkout = (TextView) findViewById(R.id.textView_Workout);
        textViewDate = (TextView) findViewById(R.id.textView_Date);
        textViewMove = (TextView) findViewById(R.id.textView_Move);
        textViewOther = (TextView) findViewById(R.id.textView_Other);

        int timeStudy = dbManager.getTime("공부");
        int timeWork = dbManager.getTime("직장");
        int timeHobby = dbManager.getTime("취미");
        int timeWorkout = dbManager.getTime("운동");
        int timeDate = dbManager.getTime("데이트");
        int timeMove = dbManager.getTime("이동");
        int timeOther = dbManager.getTime("기타");

        textViewStudy.setText(Integer.toString(timeStudy / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeStudy % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeStudy % SECONDS_PER_MINUTE) + "초");
        textViewWork.setText(Integer.toString(timeWork / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeWork % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeWork % SECONDS_PER_MINUTE) + "초");
        textViewHobby.setText(Integer.toString(timeHobby / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeHobby % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeHobby % SECONDS_PER_MINUTE) + "초");
        textViewWorkout.setText(Integer.toString(timeWorkout / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeWorkout % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeWorkout % SECONDS_PER_MINUTE) + "초");
        textViewDate.setText(Integer.toString(timeDate / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeDate % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeDate % SECONDS_PER_MINUTE) + "초");
        textViewMove.setText(Integer.toString(timeMove / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeMove % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeMove % SECONDS_PER_MINUTE) + "초");
        textViewOther.setText(Integer.toString(timeOther / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeOther % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeOther % SECONDS_PER_MINUTE) + "초");
    }
}
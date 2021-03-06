package jiyoungseok.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class TaskActivity extends AppCompatActivity {

    Double latitude;
    Double longitude;

    Button buttonStart, buttonStop, buttonReset, buttonSave;
    TextView textViewTodayDate, textViewWhatToDo;
    TextView textViewStudy, textViewWork, textViewHobby, textViewWorkout, textViewDate, textViewMove, textViewOther;
    LinearLayout startStudy, startWork, startHobby, startWorkout, startDate, startMove, startOther;
    Chronometer chronometer;

    DBManager dbManager = new DBManager(this, "myLifeLogger.db", null, 1);
    LocationManager manager;

    static final int TYPE_TASK_ACTIVITY = 1;
    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = 3600;
    static final int YEAR_TO_CONVERTDATE = 10000;
    static final int MONTH_TO_CONVERTDATE = 100;

    private int currentTime;
    private int currentTimeToSeconds;
    private int convertDate;
    private int switchWhatToDo;
    private long lastTimeBackPressed;
    private long timeWhenStopped = 0;
    private boolean isTimerRun = false;
    private int iYear, iMonth, iDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS를 켜야 위치정보를 받아올 수 있습니다.\nGPS를 켜주시기 바랍니다.").setCancelable(false).setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent gpsOptionIntent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionIntent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        Calendar today;
        today = Calendar.getInstance();
        iYear = today.get(Calendar.YEAR);
        iMonth = today.get(Calendar.MONTH);
        iDate = today.get(Calendar.DAY_OF_MONTH);

        buttonStart = (Button) findViewById(R.id.button_Start);
        buttonStop = (Button) findViewById(R.id.button_Stop);
        buttonReset = (Button) findViewById(R.id.button_Reset);
        buttonSave = (Button) findViewById(R.id.button_Save);

        textViewWhatToDo = (TextView) findViewById(R.id.textView_WhatToDo);
        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewTodayDate.setText(iYear + "년 " + (iMonth+1) + "월 " + iDate + "일");

        convertDate = iYear * YEAR_TO_CONVERTDATE + (iMonth+1) * MONTH_TO_CONVERTDATE + iDate;

        setText();

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#5D5D5D"));
        }

        buttonStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationService();

                if (latitude == null || longitude == null) {
                    Toast.makeText(TaskActivity.this, "위치정보를 확인중입니다. 잠시 후 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.stop();
                    isTimerRun = false;
                } else {
                    Toast.makeText(TaskActivity.this, "위도 : " + latitude + "\n경도 : " + longitude, Toast.LENGTH_SHORT).show();
                    chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                    chronometer.start();
                    isTimerRun = true;

                }
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

                if(chronometer.getText().toString().length() == 5) {
                    currentTime = Integer.parseInt(chronometer.getText().toString().substring(0,2) + chronometer.getText().toString().substring(3));
                    currentTimeToSeconds = ((currentTime / 100) * SECONDS_PER_MINUTE) + (currentTime % 100);
                } else if(chronometer.getText().toString().length() == 7) {
                    currentTime = Integer.parseInt(chronometer.getText().toString().substring(0,1) + chronometer.getText().toString().substring(2,4) + chronometer.getText().toString().substring(5));
                    currentTimeToSeconds = (((currentTime / 10000)) * SECONDS_PER_HOUR) + ((currentTime % 10000) / 100) * SECONDS_PER_MINUTE + (currentTime % 100);
                }

                if (isTimerRun == false) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    timeWhenStopped = 0;
                    chronometer.stop();

                    switch (switchWhatToDo) {
                        case 1:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "공부", "");
                            setText();
                            break;
                        case 2:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "직장", "");
                            setText();
                            break;
                        case 3:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "취미", "");
                            setText();
                            break;
                        case 4:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "운동", "");
                            setText();
                            break;
                        case 5:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "데이트", "");
                            setText();
                            break;
                        case 6:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "이동", "");
                            setText();
                            break;
                        case 7:
                            dbManager.insert(TYPE_TASK_ACTIVITY, convertDate, currentTimeToSeconds, latitude, longitude, "기타", "");
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

                iYear = year;
                iMonth = monthOfYear - 1;
                iDate = dayOfMonth;

                convertDate = iYear * YEAR_TO_CONVERTDATE + (iMonth+1) * MONTH_TO_CONVERTDATE + iDate;
                setText();
            }
        };
        new DatePickerDialog(TaskActivity.this, dateSetListener, iYear, iMonth, iDate).show();
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

                    startStudy.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 1;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWork:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("직장");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 2;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startHobby:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("취미");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 3;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startWorkout:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("운동");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 4;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startDate:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("데이트");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 5;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startMove:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("이동");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#B2CCFF"));
                    startOther.setBackgroundColor(Color.parseColor("#F0FFF0"));

                    switchWhatToDo = 6;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
            case R.id.startOther:
                if(isTimerRun == false) {
                    textViewWhatToDo.setText("기타");

                    startStudy.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWork.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startHobby.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startWorkout.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startDate.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startMove.setBackgroundColor(Color.parseColor("#F0FFF0"));
                    startOther.setBackgroundColor(Color.parseColor("#B2CCFF"));

                    switchWhatToDo = 7;
                    break;
                } else {
                    Toast.makeText(TaskActivity.this, "타이머가 작동중입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }


    private void startLocationService () {
        manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000;
        float minDistance = 1;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, mLocationListener);
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    public void onClickChangePage(View view) {
        Intent intent;
        switch(view.getId()) {
            case R.id.button_Task:
                intent = new Intent (getApplicationContext(), TaskActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_Log:
                intent = new Intent (getApplicationContext(), LogActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_Map:
                intent = new Intent (getApplicationContext(), MapActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.button_Goal:
                intent = new Intent (getApplicationContext(), GoalActivity.class);
                startActivity(intent);
                finish();
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

        int timeStudy = dbManager.getTime(convertDate, "공부");
        int timeWork = dbManager.getTime(convertDate, "직장");
        int timeHobby = dbManager.getTime(convertDate, "취미");
        int timeWorkout = dbManager.getTime(convertDate, "운동");
        int timeDate = dbManager.getTime(convertDate, "데이트");
        int timeMove = dbManager.getTime(convertDate, "이동");
        int timeOther = dbManager.getTime(convertDate, "기타");

        textViewStudy.setText(Integer.toString(timeStudy / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeStudy % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeStudy % SECONDS_PER_MINUTE) + "초");
        textViewWork.setText(Integer.toString(timeWork / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeWork % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeWork % SECONDS_PER_MINUTE) + "초");
        textViewHobby.setText(Integer.toString(timeHobby / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeHobby % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeHobby % SECONDS_PER_MINUTE) + "초");
        textViewWorkout.setText(Integer.toString(timeWorkout / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeWorkout % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeWorkout % SECONDS_PER_MINUTE) + "초");
        textViewDate.setText(Integer.toString(timeDate / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeDate % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeDate % SECONDS_PER_MINUTE) + "초");
        textViewMove.setText(Integer.toString(timeMove / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeMove % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeMove % SECONDS_PER_MINUTE) + "초");
        textViewOther.setText(Integer.toString(timeOther / SECONDS_PER_HOUR) + "시간 " + Integer.toString((timeOther % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE) + "분 " + Integer.toString(timeOther % SECONDS_PER_MINUTE) + "초");
    }
}
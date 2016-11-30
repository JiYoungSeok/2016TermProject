package jiyoungseok.mylifelogger;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LogActivity extends AppCompatActivity {

    Double latitude;
    Double longitude;

    TextView textViewTodayDate, textViewCurrentTime, textViewLatitude, textViewLongitude;
    EditText editTextEvent, editTextMemo;
    Button buttonCheckLocation, buttonGetImage, buttonSaveLocation;

    LocationManager manager;
    DBManager dbManager = new DBManager(this, "myLifeLogger.db", null, 1);

    final int TYPE_LOG_ACTIVITY = 2;
    final int SECONDS_PER_MINUTE = 60;
    final int SECONDS_PER_HOUR = 3600;
    final int YEAR_TO_CONVERTDATE = 10000;
    final int MONTH_TO_CONVERTDATE = 100;

    private int convertDate, convertTime;
    private int iYear, iMonth, iDate, iHour, iMinute;
    private String event, memo;
    private boolean isCheckLocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Calendar today;
        today = Calendar.getInstance();
        iYear = today.get(Calendar.YEAR);
        iMonth = today.get(Calendar.MONTH) + 1;
        iDate = today.get(Calendar.DAY_OF_MONTH);
        iHour = today.get(Calendar.HOUR_OF_DAY) + 9;
        iMinute = today.get(Calendar.MINUTE);

        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewCurrentTime = (TextView) findViewById(R.id.textView_currentTime);
        textViewLatitude = (TextView) findViewById(R.id.textView_Latitude);
        textViewLongitude = (TextView) findViewById(R.id.textView_Longitude);

        if (iHour > 24) {
           iHour = iHour - 24;
        }
        textViewTodayDate.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        textViewCurrentTime.setText("현재시간 : " + iHour + "시 " + iMinute + "분");

        editTextEvent = (EditText) findViewById(R.id.editText_Event);
        editTextMemo = (EditText) findViewById(R.id.editText_Memo);

        buttonCheckLocation = (Button) findViewById(R.id.button_CheckLocation);
        buttonGetImage = (Button) findViewById(R.id.button_GetImage);
        buttonSaveLocation = (Button) findViewById(R.id.button_SaveLocation);

        buttonCheckLocation.setOnClickListener(new Button.OnClickListener() {
            public void onClick (View v) {
                startLocationService();

                if (latitude == null || longitude == null) {
                    textViewLatitude.setText("위치 정보 확인중..\n잠시후 다시 시도하세요.");
                    textViewLongitude.setText("위치 정보 확인중..\n잠시후 다시 시도하세요.");
                    isCheckLocation = false;
                } else {
                    textViewLatitude.setText(String.valueOf(latitude));
                    textViewLongitude.setText(String.valueOf(longitude));
                    isCheckLocation = true;
                }
            }
        });

        buttonGetImage.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            }
        });

        buttonSaveLocation.setOnClickListener(new Button.OnClickListener() {
            public void onClick (View v) {
                if(isCheckLocation) {
                    convertDate = iYear * YEAR_TO_CONVERTDATE + iMonth * MONTH_TO_CONVERTDATE + iDate;
                    convertTime = iHour * SECONDS_PER_HOUR + iMinute * SECONDS_PER_MINUTE;
                    event = editTextEvent.getText().toString();
                    memo = editTextMemo.getText().toString();

                    dbManager.insert(TYPE_LOG_ACTIVITY, convertDate, convertTime, latitude, longitude, event, memo);

                    editTextEvent.setText("");
                    editTextMemo.setText("");
                    Toast.makeText(LogActivity.this, "정상적으로 저장 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LogActivity.this, "위치 확인을 먼저 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
}

package jiyoungseok.mylifelogger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    private double currentlat, currentlon;
    TextView textViewLatitude, textViewLongitude;
    Button buttonCheckLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS를 켜야 어플이 작동됩니다. GPS를 켜고 어플을 재시작 해주시기 바랍니다.").setCancelable(false).setPositiveButton("GPS 켜기", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent gpsOptionIntent = new Intent (Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionIntent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }

        init();

        textViewLatitude = (TextView) findViewById(R.id.textView_Latitude);
        textViewLongitude = (TextView) findViewById(R.id.textView_Longitude);

        buttonCheckLocation = (Button) findViewById(R.id.button_CheckLocation);

        buttonCheckLocation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewLatitude.setText(Double.toString(currentlat));
                textViewLongitude.setText(Double.toString(currentlon));
            }
        });
    }

    public void init() {
        GpsInfo gps = new GpsInfo(this);

        if (gps.isGetLocation()) {
            currentlat = gps.getLatitude();
            currentlon = gps.getLongitude();
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
}

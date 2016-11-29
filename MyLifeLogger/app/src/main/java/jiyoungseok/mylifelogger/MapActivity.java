package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LogDBManager logDBManager = new LogDBManager(this, "log.db", null, 1);

    ArrayList<LogList> myLoggerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        logDBManager.showMarker(myLoggerList);
        LatLng openMap = new LatLng(myLoggerList.get(0).getLatitude(), myLoggerList.get(1).getLongitude());

        for (int i = 0; i < myLoggerList.size(); i++) {
            LatLng marker = new LatLng(myLoggerList.get(i).getLatitude(), myLoggerList.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(marker).title(myLoggerList.get(i).getMemo()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker));

            if(i != 0) {
                mMap.addPolyline(new PolylineOptions().geodesic(true).add(new LatLng(Double.valueOf(myLoggerList.get(i - 1).getLatitude()), Double.valueOf(myLoggerList.get(i - 1).getLongitude())), new LatLng(Double.valueOf(myLoggerList.get(i).getLatitude()), Double.valueOf(myLoggerList.get(i).getLongitude()))).width(5).color(Color.RED));
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(openMap, 13));
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

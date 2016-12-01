package jiyoungseok.mylifelogger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Comparator;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LinearLayout popUpWindow;
    AlertDialog tmpAlertDialog;

    final int SECONDS_PER_MINUTE = 60;
    final int SECONDS_PER_HOUR = 3600;
    final int YEAR_TO_CONVERTDATE = 10000;
    final int MONTH_TO_CONVERTDATE = 100;
    private static int tmp;

    DBManager dbManager = new DBManager(this, "myLifeLogger.db", null, 1);

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

        dbManager.showMarker(myLoggerList);
        LatLng openMap = new LatLng(37.601876, 127.040742);

        for (int i = 0; i < myLoggerList.size(); i++) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(myLoggerList.get(i).getLatitude(), myLoggerList.get(i).getLongitude()));
            marker.title(String.valueOf(i));
            marker.snippet("자세히 보려면 말풍선을 클릭하세요.");

            mMap.addMarker(marker).showInfoWindow();

            if(i != 0) {
                mMap.
                        addPolyline(new PolylineOptions()
                        .geodesic(true)
                        .add
                                (new LatLng(Double.valueOf(myLoggerList.get(i - 1).getLatitude()), Double.valueOf(myLoggerList.get(i - 1).getLongitude())),
                                new LatLng(Double.valueOf(myLoggerList.get(i).getLatitude()), Double.valueOf(myLoggerList.get(i).getLongitude())))
                        .width(10).color(Color.RED));
            }
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(openMap, 12));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
                popUpWindow = new LinearLayout(MapActivity.this);
                popUpWindow.setPadding(0, 0, 0, 0);
                tmp = Integer.valueOf(marker.getTitle());
                Log.d("SQL", "Type : " + myLoggerList.get(tmp).getType());

                if (myLoggerList.get(tmp).getType() == 1) {
                    builder
                        .setTitle("상세정보")
                        .setCancelable(false)
                        .setMessage(
                                "분류 : Task" +
                                "\n날짜 : " + (myLoggerList.get(tmp).getDate() / YEAR_TO_CONVERTDATE) + "년 " + ((myLoggerList.get(tmp).getDate() % YEAR_TO_CONVERTDATE) / MONTH_TO_CONVERTDATE) + "월 " + (myLoggerList.get(tmp).getDate() % MONTH_TO_CONVERTDATE) + "일" +
                                "\n시간 : " + myLoggerList.get(tmp).getTime() / SECONDS_PER_MINUTE + "분 " + myLoggerList.get(tmp).getTime() % SECONDS_PER_MINUTE + "초" +
                                "\n한일 : " + myLoggerList.get(tmp).getEvent())
                        .setView(popUpWindow)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                dbManager.delete(myLoggerList.get(tmp).getLatitude());
                                Toast.makeText(MapActivity.this, "정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent moveToMap = new Intent (getApplicationContext(), MapActivity.class);
                                finish();
                                startActivity(moveToMap);
                            }
                        });

                    AlertDialog alertDialog = builder.create();
                    tmpAlertDialog = alertDialog;
                    builder.show();

                } else {
                    builder
                            .setTitle("상세정보")
                            .setCancelable(false)
                            .setMessage(
                                    "분류 : Log" +
                                    "\n날짜 : " + (myLoggerList.get(tmp).getDate() / YEAR_TO_CONVERTDATE) + "년 " + ((myLoggerList.get(tmp).getDate() % YEAR_TO_CONVERTDATE) / MONTH_TO_CONVERTDATE) + "월 " + (myLoggerList.get(tmp).getDate() % MONTH_TO_CONVERTDATE) + "일" +
                                    "\n시간 : " + myLoggerList.get(tmp).getTime() / SECONDS_PER_HOUR + "시 " + (myLoggerList.get(tmp).getTime() % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE + "분" +
                                    "\n내용 : " + myLoggerList.get(tmp).getEvent() +
                                    "\n메모 : " + myLoggerList.get(tmp).getMemo())
                            .setView(popUpWindow)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    dbManager.delete(myLoggerList.get(tmp).getLatitude());
                                    Toast.makeText(MapActivity.this, "정상적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    Intent moveToMap = new Intent (getApplicationContext(), MapActivity.class);
                                    startActivity(moveToMap);
                                    finish();
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    tmpAlertDialog = alertDialog;
                    builder.show();
                }
            }
        });
    }

    public void onClickChangePage(View view) {
        switch(view.getId()) {
            case R.id.button_Task:
                Intent moveToTask = new Intent (getApplicationContext(), TaskActivity.class);
                startActivity(moveToTask);
                finish();
                break;
            case R.id.button_Log:
                Intent moveToLog = new Intent (getApplicationContext(), LogActivity.class);
                startActivity(moveToLog);
                finish();
                break;
            case R.id.button_Map:
                Intent moveToMap = new Intent (getApplicationContext(), MapActivity.class);
                startActivity(moveToMap);
                finish();
                break;
            case R.id.button_Goal:
                Intent moveToGoal = new Intent (getApplicationContext(), GoalActivity.class);
                startActivity(moveToGoal);
                finish();
                break;
        }
    }

    public void onBackPressed() {
        Intent moveToTask = new Intent (getApplicationContext(), TaskActivity.class);
        startActivity(moveToTask);
        finish();
    }
}

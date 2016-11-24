package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

public class TaskActivity extends AppCompatActivity {

    Button buttonLog, buttonMap, buttonGoal, buttonStart, buttonStop, buttonReset;
    Button buttonStart1, buttonStart2, buttonStart3, buttonStart4, buttonStart5, buttonStart6, buttonStart7, buttonStart8, buttonStart9;
    TextView textViewTodayDate;

    private long lastTimeBackPressed;

    Today today = new Today();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        buttonLog = (Button) findViewById(R.id.button_Log);
        buttonMap = (Button) findViewById(R.id.button_Map);
        buttonGoal = (Button) findViewById(R.id.button_Goal);
        buttonStart = (Button) findViewById(R.id.button_Start);
        buttonStop = (Button) findViewById(R.id.button_Stop);
        buttonReset = (Button) findViewById(R.id.button_Reset);

        textViewTodayDate = (TextView) findViewById(R.id.textView_TodayDate);
        textViewTodayDate.setText(today.getYear() + "년 " + today.getMonth() + "월 " + today.getDay() + "일");

        final Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });

        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(),MapActivity.class);
                startActivity(intent);
            }
        });

        buttonGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), GoalActivity.class);
                startActivity(intent);
            }
        });

        buttonStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.start();
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
            }
        });


        buttonReset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
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

package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoalActivity extends AppCompatActivity {

    Button buttonTask, buttonLog, buttonMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        buttonTask = (Button) findViewById(R.id.button_Task);
        buttonLog = (Button) findViewById(R.id.button_Log);
        buttonMap = (Button) findViewById(R.id.button_Map);

        buttonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), TaskActivity.class);
                startActivity(intent);
            }
        });

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), LogActivity.class);
                startActivity(intent);
            }
        });
    }
}

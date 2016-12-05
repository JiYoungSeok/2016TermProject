package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.parseColor("#5D5D5D"));
        }
    }

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
            case R.id.button_MakeGoal:
                intent = new Intent (getApplicationContext(), MakeGoalActivity.class);
                startActivity(intent);
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

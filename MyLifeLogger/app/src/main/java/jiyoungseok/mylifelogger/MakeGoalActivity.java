package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MakeGoalActivity extends AppCompatActivity {

    EditText editTextGoalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_goal);

        editTextGoalTime = (EditText) findViewById (R.id.editText_GoalTime);

        editTextGoalTime.setHint("시간을 입력하세요");
    }

    public void onBackPressed() {
        Intent moveToGoal = new Intent (getApplicationContext(), GoalActivity.class);
        startActivity(moveToGoal);
        finish();
    }
}

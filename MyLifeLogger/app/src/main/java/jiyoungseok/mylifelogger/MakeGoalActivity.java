package jiyoungseok.mylifelogger;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MakeGoalActivity extends AppCompatActivity {

    EditText editTextGoalTime;
    Spinner spinner;

    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_goal);

        editTextGoalTime = (EditText) findViewById (R.id.editText_GoalTime);
        spinner = (Spinner) findViewById (R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        editTextGoalTime.setHint("시간을 입력하세요");
    }

    public void onBackPressed() {
        Intent moveToGoal = new Intent (getApplicationContext(), GoalActivity.class);
        startActivity(moveToGoal);
        finish();
    }
}

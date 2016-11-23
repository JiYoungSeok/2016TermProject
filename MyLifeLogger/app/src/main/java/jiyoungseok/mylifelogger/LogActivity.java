package jiyoungseok.mylifelogger;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

public class LogActivity extends AppCompatActivity {

    Button buttonTask, buttonMap, buttonGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        buttonTask = (Button) findViewById(R.id.button_Task);
        buttonMap = (Button) findViewById(R.id.button_Map);
        buttonGoal = (Button) findViewById(R.id.button_Goal);

        buttonTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), TaskActivity.class);
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
    }
}

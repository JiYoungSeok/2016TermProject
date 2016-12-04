package jiyoungseok.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MakeGoalActivity extends AppCompatActivity {

    TextView textViewStartDay, textViewEndDay;
    EditText editTextGoalTime;
    Spinner spinner;
    RadioButton radioButtonUp, radioButtonDown;
    Button buttonSaveGoal;
    ListView listViewGoal;
    String category;

    DBManagerGoal dbManager = new DBManagerGoal(this, "myGoal.db", null, 1);

    ArrayList<GoalList> myGoalList = new ArrayList<>();

    static final String IS_CHECKED_UP = "이상";
    static final String IS_CHECKED_DOWN = "이하";
    static final int YEAR_TO_CONVERTDATE = 10000;
    static final int MONTH_TO_CONVERTDATE = 100;

    private int convertStartDate;
    private int convertEndDate;
    private int startYear, startMonth, startDate;
    private int endYear, endMonth, endDate;
    private int isCheckUpOrDown;

    ListViewAdapter listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_goal);

        Calendar today;
        today = Calendar.getInstance();
        startYear = today.get(Calendar.YEAR);
        startMonth = today.get(Calendar.MONTH);
        startDate = today.get(Calendar.DAY_OF_MONTH);
        endYear = today.get(Calendar.YEAR);
        endMonth = today.get(Calendar.MONTH);
        endDate = today.get(Calendar.DAY_OF_MONTH);

        textViewStartDay = (TextView) findViewById(R.id.textView_StartDay);
        textViewEndDay = (TextView) findViewById(R.id.textView_EndDay);
        textViewStartDay.setText(startYear + "년 " + (startMonth+1) + "월 " + startDate + "일");
        textViewEndDay.setText(endYear + "년 " + (endMonth+1) + "월 " + endDate + "일");

        spinner = (Spinner) findViewById (R.id.spinner);
        editTextGoalTime = (EditText) findViewById (R.id.editText_GoalTime);
        editTextGoalTime.setHint("시간을 입력하세요");

        radioButtonUp = (RadioButton) findViewById (R.id.radioButton_Up);
        radioButtonDown = (RadioButton) findViewById (R.id.radioButton_Down);
        buttonSaveGoal = (Button) findViewById (R.id.button_SaveGoal);
        listViewGoal = (ListView) findViewById(R.id.listView_Goal);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
                ((TextView)parent.getChildAt(0)).setTextColor(Color.BLACK);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        RadioButton.OnClickListener optionOnClickListener = new RadioButton.OnClickListener() {
            public void onClick(View view) {
                if (radioButtonUp.isChecked()) {
                    isCheckUpOrDown = 1;
                } else {
                    isCheckUpOrDown = 2;
                }
            }
        };

        radioButtonUp.setOnClickListener(optionOnClickListener);
        radioButtonDown.setOnClickListener(optionOnClickListener);

        showListView();

        buttonSaveGoal.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                convertStartDate = startYear * YEAR_TO_CONVERTDATE + (startMonth+1) * MONTH_TO_CONVERTDATE + startDate;
                convertEndDate = endYear * YEAR_TO_CONVERTDATE + (endMonth+1) * MONTH_TO_CONVERTDATE + endDate;

                if (isCheckUpOrDown == 1) {
                    dbManager.insert(convertStartDate, convertEndDate, Integer.parseInt(editTextGoalTime.getText().toString()), category, IS_CHECKED_UP);
                    showListView();

                } else if (isCheckUpOrDown == 2) {
                    dbManager.insert(convertStartDate, convertEndDate, Integer.parseInt(editTextGoalTime.getText().toString()), category, IS_CHECKED_DOWN);
                    showListView();
                }
            }
        });

    }

    public void onClickStartCalendar(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                textViewStartDay = (TextView) findViewById(R.id.textView_StartDay);
                textViewStartDay.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");

                startYear = year;
                startMonth = monthOfYear - 1;
                startDate = dayOfMonth;
            }
        };
        new DatePickerDialog(MakeGoalActivity.this, dateSetListener, startYear, startMonth, startDate).show();
    }

    public void onClickEndCalendar(View view) {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear = monthOfYear + 1;

                textViewEndDay = (TextView) findViewById(R.id.textView_EndDay);
                textViewEndDay.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");

                endYear = year;
                endMonth = monthOfYear - 1;
                endDate = dayOfMonth;
            }
        };
        new DatePickerDialog(MakeGoalActivity.this, dateSetListener, endYear, endMonth, endDate).show();
    }

    public void showListView () {
        myGoalList.clear();
        dbManager.showList(myGoalList);
        listViewAdapter = new ListViewAdapter(MakeGoalActivity.this, myGoalList, R.layout.goal_row);
        listViewGoal.setAdapter(listViewAdapter);

    }

    public void onBackPressed() {
        Intent moveToGoal = new Intent (getApplicationContext(), GoalActivity.class);
        startActivity(moveToGoal);
        finish();
    }
}

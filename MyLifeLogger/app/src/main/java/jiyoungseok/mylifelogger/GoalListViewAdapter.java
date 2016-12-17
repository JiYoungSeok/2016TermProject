package jiyoungseok.mylifelogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GoalListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<GoalList> gl;
    int layout;
    LayoutInflater inf;

    static final int SECONDS_PER_MINUTE = 60;
    static final int SECONDS_PER_HOUR = 3600;

   DBManager dbManager;

    public GoalListViewAdapter(Context context, ArrayList<GoalList> gl, int layout) {
        this.context = context;
        this.gl = gl;
        this.layout = layout;
        dbManager = new DBManager(context, "myLifeLogger.db", null, 1);
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gl.size();
    }

    @Override
    public Object getItem (int position) { return gl.get(position); }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        TextView textViewGoalDate = (TextView) convertView.findViewById(R.id.textView_GoalDate);
        TextView textViewCategory = (TextView) convertView.findViewById(R.id.textView_Category);
        TextView textViewGoalTime = (TextView) convertView.findViewById(R.id.textView_GoalTime);
        TextView textViewDoTime = (TextView) convertView.findViewById(R.id.textView_DoTime);
        TextView textViewPercentage = (TextView) convertView.findViewById(R.id.textView_Percentage);

        DecimalFormat form = new DecimalFormat("#.###");
        GoalList goalList = gl.get(position);
        String startDate = Integer.toString(goalList.getStartDate());
        String endDate = Integer.toString(goalList.getEndDate());
        int doTime = dbManager.getDoTime(goalList.getStartDate(), goalList.getEndDate(), goalList.getCategory());
        double percentage = (double) doTime / (goalList.getTime() * SECONDS_PER_HOUR);

        textViewGoalDate.setText(startDate.substring(0,4) + "년 " + startDate.substring(4,6) + "월 " + startDate.substring(6) + "일 ~ " + endDate.substring(0,4) + "년 " + endDate.substring(4,6) + "월 " + endDate.substring(6) + "일");
        textViewCategory.setText(goalList.getCategory());
        textViewGoalTime.setText(goalList.getTime() + "시간 " + goalList.getUpOrDown());
        textViewDoTime.setText(doTime / SECONDS_PER_HOUR + "시간 " + (doTime % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE + "분 " + doTime % SECONDS_PER_MINUTE + "초");
        textViewPercentage.setText(form.format(percentage) + " %");

        return convertView;
    }
}

package jiyoungseok.mylifelogger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter{
    Context context;
    ArrayList<GoalList> gl;
    int layout;
    LayoutInflater inf;

    public ListViewAdapter(Context context, ArrayList<GoalList> gl, int layout) {
        this.context = context;
        this.gl = gl;
        this.layout = layout;
        inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gl.size();
    }

    @Override
    public Object getItem (int position) {
        return gl.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inf.inflate(layout, null);
        }

        TextView tv1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView2);
        TextView tv3 = (TextView) convertView.findViewById(R.id.textView3);

        GoalList goalList = gl.get(position);
        String startDate = Integer.toString(goalList.getStartDate());
        String endDate = Integer.toString(goalList.getEndDate());

        tv1.setText(startDate.substring(0,4) + "년 " + startDate.substring(4,6) + "월 " + startDate.substring(6) + "일 ~ " + endDate.substring(0,4) + "년 " + endDate.substring(4,6) + "월 " + endDate.substring(6) + "일");
        tv2.setText(goalList.getCategory());
        tv3.setText(goalList.getTime() + "시간 " + goalList.getUpOrDown());

        return convertView;
    }
}

package erostamas.brewer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by etamero on 2016.06.30..
 */

public class SegmentListAdapter extends BaseAdapter{
    private static ArrayList<Segment> segments;
    public SegmentListAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return segments.size();
    }

    @Override
    public Segment getItem(int i) {
        return segments.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.segment_list_item, parent, false);
        } else {
            result = convertView;
        }
        TextView temp_text = (TextView) result.findViewById(R.id.segment_temp);
        TextView duration_text = (TextView) result.findViewById(R.id.segment_duration);

        temp_text.setText(Double.toString(getItem(position)._temp) + "    °C");
        duration_text.setText(String.format("%02d", getItem(position)._hours) + ":" + String.format("%02d", getItem(position)._minutes));
        return result;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public void notifyDataSetChanged() {
        segments = DisplayCurvesFragment.curves.get(DisplaySegmentsActivity._curveName).getSegments();
        super.notifyDataSetChanged();
    }
}

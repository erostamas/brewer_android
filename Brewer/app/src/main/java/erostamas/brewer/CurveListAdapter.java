package erostamas.brewer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by etamero on 2016.06.30..
 */

public class CurveListAdapter extends BaseAdapter {

    public CurveListAdapter() {
        notifyDataSetChanged();
    }
    private static ArrayList<String> curveNames = new ArrayList<>();

    @Override
    public int getCount() {
        return curveNames.size();
    }

    @Override
    public String getItem(int i) {
        return curveNames.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.curve_list_item, parent, false);
        } else {
            result = convertView;
        }
        TextView textView = (TextView) result.findViewById(R.id.curve_name);
        textView.setText(getItem(position));
        return result;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public void notifyDataSetChanged() {
        curveNames.clear();
        for (String curve : DisplayCurvesFragment.curves.keySet()) {
            curveNames.add(curve);
        }
        Collections.sort(curveNames);
        super.notifyDataSetChanged();
    }
}


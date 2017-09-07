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
    public ArrayList<String> curvenames = new ArrayList<String>();
    HashMap<String, ArrayList<Segment>> map;
    public CurveListAdapter(HashMap<String, ArrayList<Segment>> _map) {
        map = _map;
        for (String curve : map.keySet()) {
            curvenames.add(curve);
            Log.i("mapadapter", "added curvename: " + curve);
        }
        Collections.sort(curvenames);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        Log.i("mapadapter", "getcount called: " + curvenames.size());
        return curvenames.size();
    }

    @Override
    public String getItem(int i) {
        return curvenames.get(i);
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
        for (String curve : map.keySet()) {
            curvenames.add(curve);
            Log.i("mapadapter", "added curvename: " + curve);
        }
        Collections.sort(curvenames);
        super.notifyDataSetChanged();
    }
}


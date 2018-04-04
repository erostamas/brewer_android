package erostamas.brewer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static erostamas.brewer.MainActivity.mainActivity;

/**
 * Created by etamero on 2016.06.30..
 */

public class MiscDataListAdapter extends BaseAdapter{
    public MiscDataListAdapter() {
        notifyDataSetChanged();
    }
    ArrayList<MiscData> _miscData = new ArrayList<>();
    @Override
    public int getCount() {
        return _miscData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.misc_data_list_item, parent, false);
        } else {
            result = convertView;
        }
        TextView name_text = (TextView) result.findViewById(R.id.data_name);
        TextView value_text = (TextView) result.findViewById(R.id.data_value);

        name_text.setText(_miscData.get(position)._name);
        value_text.setText(_miscData.get(position)._value);
        return result;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public void notifyDataSetChanged() {
        _miscData.clear();
        _miscData.add(new MiscData("MODE", ControlFragment.currentMode));
        _miscData.add(new MiscData("OUTPUT", Integer.toString(mainActivity.currentOutput)));
        if(ControlFragment.currentMode.equals("AUTO")) {
            _miscData.add(new MiscData("CURRENT CURVE", ControlFragment.currentCurveName));
            _miscData.add(new MiscData("TIME TO NEXT SP", Integer.toString(mainActivity.timeToNextSetpoint)));
        }
        super.notifyDataSetChanged();
    }
}

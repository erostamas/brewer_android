package erostamas.brewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by etamero on 2016.06.30..
 */

public class DisplayCurvesFragment extends Fragment {
    public static final String CURVE_NAME = "com.example.brewer.CURVE_NAME";
    public static HashMap<String, ArrayList<Segment>> curves = new HashMap<>();
    public static CurveListAdapter curveListAdapter = new CurveListAdapter();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static boolean initialized = false;
    public DisplayCurvesFragment() {
        Log.i("brewer", "DisplayCurvesFragment constructorz");
        if (!initialized) {
            Log.i("brewer", "Adding sim curves");
            initialized = true;
            ArrayList<Segment> curve = new ArrayList<Segment>();
            curve.add(new Segment(65, 3));
            curve.add(new Segment(70, 5));
            curve.add(new Segment(45.5, 5));
            ArrayList<Segment> curve2 = new ArrayList<Segment>();
            curve2.add(new Segment(10, 3));
            curve2.add(new Segment(20, 5));
            curve2.add(new Segment(30.5, 5));
            DisplayCurvesFragment.curves.put("curve 1", curve);
            DisplayCurvesFragment.curves.put("curve 2", curve2);
            curveListAdapter.notifyDataSetChanged();
        }
    }

    public static DisplayCurvesFragment newInstance(int sectionNumber) {
        DisplayCurvesFragment fragment = new DisplayCurvesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.display_curves, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.curves_list);
        listView.setAdapter(curveListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position, long id){
                String curve = curveListAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), DisplaySegmentsActivity.class);
                intent.putExtra(CURVE_NAME, curve);
                startActivity(intent);

            }
        });
        ((AppCompatActivity)(getActivity())).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.display_curves_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.add_curve) {
            Intent intent = new Intent(getActivity(), AddCurveActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

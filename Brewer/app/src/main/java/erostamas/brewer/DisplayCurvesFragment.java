package erostamas.brewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by etamero on 2016.06.30..
 */

public class DisplayCurvesFragment extends Fragment {
    public static final String CURVE_NAME = "com.example.brewer.CURVE_NAME";
    public static HashMap<String, Curve> curves = new HashMap<>();
    public static CurveListAdapter curveListAdapter = new CurveListAdapter();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static boolean initialized = false;
    public DisplayCurvesFragment() {
        curveListAdapter.notifyDataSetChanged();
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
        int[] colors = {0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);
        registerForContextMenu(listView);

        ((AppCompatActivity)(getActivity())).getSupportActionBar().setDisplayShowHomeEnabled(true);
        setHasOptionsMenu(true);

        ImageButton fab = (ImageButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCurveActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.display_curves_menu, menu);
        menu.add(0, 0, 0, "Settings");
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.share_curves) {
            String curvesString = "";
            Iterator it = DisplayCurvesFragment.curves.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                curvesString += pair.getValue().toString() + "\n";
            }
            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, curvesString);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.curves_list){

            MenuItem mnu1=menu.add(0,0,0,"Edit");
            MenuItem mnu2=menu.add(0,1,1,"Delete");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        String selectedCurve = curveListAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(getActivity(), AddCurveActivity.class);
                intent.putExtra(CURVE_NAME, selectedCurve);
                startActivity(intent);
                break;
            case 1:
                curves.remove(selectedCurve);
                curveListAdapter.notifyDataSetChanged();
                break;

            default:
                break;

        }
        return true;
    }
}

package erostamas.brewer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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
        Log.i("brewer", "DisplayCurvesFragment constructorz");
        if (!initialized) {
            Log.i("brewer", "Adding sim curves");
            initialized = true;
            Curve curve = new Curve("curve_1");
            curve.add(new Segment(65, 0, 5));
            curve.add(new Segment(70, 0, 6));
            curve.add(new Segment(45, 0, 7));
            Curve curve2 = new Curve("curve_2");
            curve2.add(new Segment(10, 0, 8));
            curve2.add(new Segment(20, 0, 9));
            curve2.add(new Segment(30, 2, 4));
            DisplayCurvesFragment.curves.put(curve.getName(), curve);
            DisplayCurvesFragment.curves.put(curve2.getName(), curve2);
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
        int[] colors = {0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);
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
        inflater.inflate(R.menu.display_curves_menu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        String filename = "brewer_curves.txt";
        FileOutputStream outputStream;
        Log.i("brewer", "Writing file");
        try {
            outputStream =  getActivity().getApplicationContext().openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write("hello".getBytes());
            outputStream.close();
            Log.i("brewer", "Writing file done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            InputStream instream = new FileInputStream("myfilename.txt");
            if (instream != null) {
                // prepare the file for reading
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);

                String line;

                // read every line of the file into the line-variable, on line at the time
                do {
                    line = buffreader.readLine();
                    // do something with the line
                } while (line != null);
                instream.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

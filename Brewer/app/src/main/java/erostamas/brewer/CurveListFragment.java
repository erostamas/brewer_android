package erostamas.brewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import static erostamas.brewer.MainActivity.SIMULATION_MODE;
import static erostamas.brewer.MainActivity.current_curve;
import static erostamas.brewer.MainActivity.curvelistadapter;
import static erostamas.brewer.MainActivity.mainActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public class CurveListFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";


    public static CurveListFragment newInstance(int sectionNumber) {
        CurveListFragment fragment = new CurveListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public CurveListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);//Make sure you have this line of code.
        View rootView = inflater.inflate(R.layout.fragment_curves, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.curves_list);
        curvelistadapter = new CurveListAdapter(MainActivity.curves);
        listView.setAdapter(curvelistadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                current_curve = curvelistadapter.curvenames.get(i);
                Log.i("segments", "current_curve set to: " + current_curve);
                Intent intent = new Intent(getActivity(), DisplaySegmentsActivity.class);
                startActivity(intent);
            }
        });
        if(SIMULATION_MODE) {
            //mainActivity.simulateCurves();
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.display_curves_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

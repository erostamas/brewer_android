package erostamas.brewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
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
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
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
            mainActivity.simulateCurves();
        }
        return rootView;
    }

}

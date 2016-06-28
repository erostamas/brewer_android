package erostamas.brewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static erostamas.brewer.MainActivity.curvelistadapter;

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
        curvelistadapter = new ArrayAdapter<String>(MainActivity.mainActivity,
                android.R.layout.simple_list_item_1,
                MainActivity.curves);
        listView.setAdapter(curvelistadapter);
        return rootView;
    }

}

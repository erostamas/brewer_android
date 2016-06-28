package erostamas.brewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by etamero on 2016.06.28..
 */
public class ControlFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ControlFragment newInstance(int sectionNumber) {
        ControlFragment fragment = new ControlFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ControlFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        MainActivity.controlFragmentView = rootView;
        final ImageButton inc_button = (ImageButton) rootView.findViewById(R.id.increase_setpoint_button);
        inc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.mainActivity.increaseSetpoint();
            }
        });

        final ImageButton dec_button = (ImageButton) rootView.findViewById(R.id.decrease_setpoint_button);
        dec_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.mainActivity.decreaseSetpoint();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Brewer", "control fragment created");
        MainActivity.connectionEstablished();
    }
}

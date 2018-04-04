package erostamas.brewer;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import erostamas.brewer.Views.Gauge;

import static android.content.Context.WIFI_SERVICE;
import static erostamas.brewer.MainActivity.controlFragmentView;
import static erostamas.brewer.MainActivity.currentSetpoint;
import static erostamas.brewer.MainActivity.currentTemperature;
import static erostamas.brewer.MainActivity.mainActivity;
import static erostamas.brewer.MainActivity.previousSetpoint;

/**
 * Created by etamero on 2016.06.28..
 */
public class ControlFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static ScheduledExecutorService scheduleTaskExecutor;
    private MiscDataListAdapter miscDataListAdapter = new MiscDataListAdapter();
    private Timer uiUpdateTimer;
    public static String currentCurveName = "";
    public static String currentMode = "";
    private static ScheduledFuture<?> scheduledFuture;
    private static Context mContext;
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
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_control, container, false);
        controlFragmentView = rootView;

        NumberPicker setpointPicker = (NumberPicker) controlFragmentView.findViewById(R.id.setpoint);
        setpointPicker.setMinValue(0);
        setpointPicker.setMaxValue(100);
        setpointPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                UdpMessage msg = new UdpMessage(mainActivity.brewerAddress, 50001, "setpoint " + Integer.toString(numberPicker.getValue()));
                UdpSender sender = new UdpSender();
                sender.execute(msg);
            }
        });
        ListView miscDataList = (ListView) controlFragmentView.findViewById(R.id.misc_data_list);
        miscDataList.setAdapter(miscDataListAdapter);
        int[] colors = {0, 0xFFFFFFFF, 0};
        miscDataList.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        miscDataList.setDividerHeight(1);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restartDownloadTask();

        uiUpdateTimer = new Timer();
        uiUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        updateUI();
                    }
                });
            }
        }, 0, 1000);

    }

    public static void restartDownloadTask() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

        scheduledFuture = scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String[] urls = {"http://" + mainActivity.brewerAddress + "/process_data.php"};
                ProcessDataDownloader downloader = new ProcessDataDownloader(mContext);
                downloader.execute(urls);
            }
        }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.
    }

    private void updateUI() {
        if (controlFragmentView == null || currentTemperature == -1.0) {
            return;
        }
        Gauge tempview = (Gauge) controlFragmentView.findViewById(R.id.current_temp);
        tempview.set(currentTemperature);

        miscDataListAdapter.notifyDataSetChanged();

        NumberPicker setpointview = (NumberPicker) controlFragmentView.findViewById(R.id.setpoint);
        if (currentSetpoint != previousSetpoint) {
            previousSetpoint = currentSetpoint;
            setpointview.setValue(currentSetpoint);
        }
    }
}

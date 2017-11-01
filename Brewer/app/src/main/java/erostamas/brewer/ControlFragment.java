package erostamas.brewer;

import android.content.DialogInterface;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import erostamas.brewer.Views.Gauge;

import static android.content.Context.WIFI_SERVICE;
import static erostamas.brewer.MainActivity.controlFragmentView;
import static erostamas.brewer.MainActivity.mainActivity;

/**
 * Created by etamero on 2016.06.28..
 */
public class ControlFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ScheduledExecutorService scheduleTaskExecutor;
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
        controlFragmentView = rootView;
        final ImageButton inc_button = (ImageButton) rootView.findViewById(R.id.increase_setpoint_button);
        inc_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UdpMessage msg = new UdpMessage(mainActivity.brewerAddress, 50001, "inc_setpoint");
                UdpSender sender = new UdpSender();
                sender.execute(msg);
            }
        });

        final ImageButton dec_button = (ImageButton) rootView.findViewById(R.id.decrease_setpoint_button);
        dec_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UdpMessage msg = new UdpMessage(mainActivity.brewerAddress, 50001, "dec_setpoint");
                UdpSender sender = new UdpSender();
                sender.execute(msg);
            }
        });

        Gauge setpointview = (Gauge) controlFragmentView.findViewById(R.id.setpoint);
        setpointview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter setpoint");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.callOnClick();
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int setpoint = Integer.parseInt(input.getText().toString());
                        UdpMessage msg = new UdpMessage(mainActivity.brewerAddress, 50001, "setpoint " + Integer.toString(setpoint));
                        UdpSender sender = new UdpSender();
                        sender.execute(msg);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                input.callOnClick();
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WifiManager manager = (WifiManager) mainActivity.getApplicationContext().getSystemService(WIFI_SERVICE);
        final DhcpInfo dhcp = manager.getDhcpInfo();
        byte[] addressBytes = new byte[4];
        for (int k = 0; k < 4; k++) {
            addressBytes[k] = (byte) ((dhcp.gateway >> k * 8) & 0xFF);
        }
        try {
            final InetAddress brewerIpAddress = InetAddress.getByAddress(addressBytes);
            mainActivity.brewerAddress = brewerIpAddress.getHostAddress();
            scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    String[] urls = {"http://" + brewerIpAddress.getHostAddress() + "/data.xml"};
                    ProcessDataDownloader downloader = new ProcessDataDownloader();
                    downloader.execute(urls);
                }
            }, 0, 1, TimeUnit.SECONDS); // or .MINUTES, .HOURS etc.
            Log.i("Brewer", "6");
        } catch (UnknownHostException ex) {
            Log.e("brewer", "Unknown host exception");
        }
    }
}

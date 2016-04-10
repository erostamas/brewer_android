package erostamas.brewer;

import java.net.DatagramSocket;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static DatagramSocket udpSocket;
    ViewPager mViewPager;
    static Context myappcontext;
    public static String brewerAddress = "";
    public static View controlFragmentView;
    public static TcpInterface tcpInterface;
    public static double currentTemperature;
    public static double setpoint;
    public static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;
        myappcontext = getApplicationContext();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.i("Brewer", "main activity created");

    }
    public static void updateUI(){
        TextView tv = (TextView) controlFragmentView.findViewById(R.id.current_temp);
        tv.setText("" + currentTemperature);

        tv = (TextView) controlFragmentView.findViewById(R.id.setpoint);
        tv.setText("" + setpoint);
    }

    public static void connectionEstablished()
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Connected to brewer at: " + MainActivity.brewerAddress, Toast.LENGTH_SHORT).show();
                        TextView tv = (TextView) controlFragmentView.findViewById(R.id.current_temp);
                        tv.setText("brewer is at: " + MainActivity.brewerAddress);
                    }
                }
        );
        // connect through tcp
        new connectTask().execute("");
        while(tcpInterface == null){}
        tcpInterface.sendMessage("get_temperature");
        ControlThread controlThread = new ControlThread();
        controlThread.run();
        Log.i("Brewer", "sent temp request");
    }

    public static Context getContext() {
        return myappcontext;
    }

    public static void setBrewerAddress(String address) {
        MainActivity.brewerAddress = address;

    }

    public void increaseSetpoint(){
        MainActivity.tcpInterface.sendMessage("inc_setpoint");
    }

    public void decreaseSetpoint(){
        MainActivity.tcpInterface.sendMessage("dec_setpoint");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    public static class connectTask extends AsyncTask<String,String,TcpInterface> {

        @Override
        protected TcpInterface doInBackground(String... message) {

            //we create a TCPClient object and
            MainActivity.tcpInterface = new TcpInterface(new TcpInterface.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    if (message.contains("temp:")){
                        currentTemperature = Double.parseDouble(message.substring(6));
                    } else if (message.contains("sp:")){
                        setpoint = Double.parseDouble(message.substring(4));
                    }

                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.updateUI();
                        }
                    });
                    Log.i("Brewer", "recieved message: " + message);
                }
            });
            tcpInterface.run();

            return null;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position != 0) {
                return PlaceholderFragment.newInstance(position + 1);
            } else {
            return ControlFragment.newInstance(position);}
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public static class ControlFragment extends Fragment {
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
            controlFragmentView = rootView;
            final ImageButton inc_button = (ImageButton) rootView.findViewById(R.id.increase_setpoint_button);
            inc_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mainActivity.increaseSetpoint();
                }
            });

            final ImageButton dec_button = (ImageButton) rootView.findViewById(R.id.decrease_setpoint_button);
            dec_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mainActivity.decreaseSetpoint();
                }
            });

            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.i("Brewer", "control fragment created");
            if (MainActivity.brewerAddress == "") {
                UdpDiscoveryThread udpDiscoveryThread = new UdpDiscoveryThread();
                udpDiscoveryThread.start();
            }
        }
    }


}

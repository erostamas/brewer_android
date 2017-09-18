package erostamas.brewer;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import erostamas.brewer.Views.TemperatureGauge;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    public static boolean SIMULATION_MODE = true;
    SectionsPagerAdapter mSectionsPagerAdapter;
    public static DatagramSocket udpSocket;
    ViewPager mViewPager;
    static Context myappcontext;
    public static String brewerAddress = "172.24.1.1";
    public static View controlFragmentView;
    public static double currentTemperature;
    public static String currentMode;
    public static double currentSetpoint;
    public static double currentOutput;
    public static MainActivity mainActivity;
    public static HashMap<String, ArrayList<Segment>> curves = new HashMap<>();
    public static CurveListAdapter curvelistadapter;
    public static SegmentListAdapter segmentlistadapter;
    public static String current_curve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;
        myappcontext = getApplicationContext();
        curves.clear();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        Log.i("Brewer", "main activity created");

    }
    public static void updateUI(){

        TemperatureGauge tempview = (TemperatureGauge) controlFragmentView.findViewById(R.id.current_temp);
        tempview.set(currentTemperature);

        TextView tv = (TextView) controlFragmentView.findViewById(R.id.current_mode);
        tv.setText("MODE: " + currentMode);

        TemperatureGauge setpointview = (TemperatureGauge) controlFragmentView.findViewById(R.id.setpoint);
        setpointview.set(currentSetpoint);

        //tv = (TextView) controlFragmentView.findViewById(R.id.current_output);
        //tv.setText("" + currentOutput);
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
                return CurveListFragment.newInstance(position + 1);
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

    public void simulateCurves() {
        ArrayList<Segment> curve = new ArrayList<Segment>();
        curve.add(new Segment(65,3));
        curve.add(new Segment(70,5));
        curve.add(new Segment(45.5,5));
        curves.put("curve 1", curve);
        curves.put("curve 2", curve);
        curvelistadapter.notifyDataSetChanged();
    }
}

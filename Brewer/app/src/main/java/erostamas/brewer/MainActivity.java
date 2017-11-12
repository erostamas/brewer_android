package erostamas.brewer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.PersistableBundle;
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
import android.widget.NumberPicker;

import erostamas.brewer.Views.DataDisplay;
import erostamas.brewer.Views.Gauge;

import static erostamas.brewer.DisplayCurvesFragment.curves;

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
    ViewPager mViewPager;
    static Context myappcontext;
    public static String brewerAddress = "172.24.1.1";
    public static View controlFragmentView;
    public static double currentTemperature;
    public static String currentMode;
    public static int currentSetpoint;
    public static int currentOutput;
    public static double nextSetpoint;
    public static int timeToNextSetpoint;
    public static MainActivity mainActivity;
    public static CurveListAdapter curvelistadapter;
    public static SegmentListAdapter segmentlistadapter;
    public static String current_curve;
    private static int position = 0;
    static int previousSetpoint = -1;
    private static String curveFileName = "brewer_curves.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;
        myappcontext = getApplicationContext();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        loadCurvesFromFile();

    }
    public static void updateUI(){

        //DataDisplay outputview = (DataDisplay) controlFragmentView.findViewById(R.id.current_output);
        //outputview.set(Integer.toString(currentOutput));

        //DataDisplay nextsetpointview = (DataDisplay) controlFragmentView.findViewById(R.id.next_setpoint);
        //DataDisplay timetonextsetpointview = (DataDisplay) controlFragmentView.findViewById(R.id.time_to_next_segment);
        //if (currentMode == "AUTO") {
        //    nextsetpointview.set(String.format("%.1f", nextSetpoint));
        //    timetonextsetpointview.set(Double.toString(timeToNextSetpoint));
        //} else {
        //    nextsetpointview.set("");
        //    timetonextsetpointview.set("");
        //}
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        position = mViewPager.getCurrentItem();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(position);
    }

    @Override
    protected void onPause() {
        super.onPause();
        position = mViewPager.getCurrentItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileOutputStream outputStream;
        try {
            outputStream =  getApplicationContext().openFileOutput(curveFileName, Context.MODE_PRIVATE);
            Iterator it = DisplayCurvesFragment.curves.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                outputStream.write(pair.getValue().toString().getBytes());
                outputStream.write("\n".getBytes());
            }

            outputStream.close();
            Log.i("brewer_file", "Writing file done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        mViewPager.setCurrentItem(position);

    }

    private void loadCurvesFromFile() {
        try {
            InputStream instream = getApplicationContext().openFileInput(curveFileName);
            if (instream != null) {
                // prepare the file for reading
                InputStreamReader inputReader = new InputStreamReader(instream);
                BufferedReader buffReader = new BufferedReader(inputReader);

                String line;

                // read every line of the file into the line-variable, on line at the time
                do {

                    line = buffReader.readLine();
                    if (line != null) {
                        Curve curve = new Curve(line);
                        DisplayCurvesFragment.curves.put(curve.getName(), curve);
                    }
                } while (line != null);
                instream.close();
                DisplayCurvesFragment.curveListAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                return DisplayCurvesFragment.newInstance(position + 1);
            } else {
            return ControlFragment.newInstance(position);}
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
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

}

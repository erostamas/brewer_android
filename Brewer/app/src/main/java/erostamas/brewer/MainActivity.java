package erostamas.brewer;

import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.List;
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
import android.widget.ArrayAdapter;
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
    public static String brewerAddress = "172.24.1.1";
    public static View controlFragmentView;
    public static TcpInterface tcpInterface;
    public static double currentTemperature;
    public static double setpoint;
    public static MainActivity mainActivity;
    public static List<String> curves = new ArrayList<String>();
    public static ArrayAdapter curvelistadapter;

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

        tcpInterface = new TcpInterface(new TcpInterface.OnMessageReceived() {
            @Override
            //here the messageReceived method is implemented
            public void messageReceived(String message) {
                try {
                    Log.i("msg", "recieved message: " + message);
                    //this method calls the onProgressUpdate
                    if (message.contains("temp:")) {
                        currentTemperature = Double.parseDouble(message.substring(6));
                    } else if (message.contains("sp:")) {
                        setpoint = Double.parseDouble(message.substring(4));
                    } else if (message.contains("curves:")) {
                        String curves_str = message.substring(8);
                        String[] separated = curves_str.split(";");
                        for (int i = 0; i < separated.length; i++) {
                            curves.add(separated[i]);
                        }
                        mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                curvelistadapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (Exception e) {}
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.updateUI();
                    }
                });
                Log.i("Brewer", "recieved message: " + message);
            }
        });

    }
    public static void updateUI(){

        TextView tv = (TextView) controlFragmentView.findViewById(R.id.current_temp);
        tv.setText("" + currentTemperature);

        tv = (TextView) controlFragmentView.findViewById(R.id.setpoint);
        tv.setText("" + setpoint);

        tv = (TextView) controlFragmentView.findViewById(R.id.connection_state);

        if(tcpInterface.connected) {
            tv.setText("CONNECTED");
            mainActivity.setTitle("CONNECTED");
            mainActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            //mainActivity.getSupportActionBar().setDisplayUseLogoEnabled(true);
            mainActivity.getSupportActionBar().setIcon(R.drawable.connected);
        } else {
            tv.setText("DISCONNECTED");
            mainActivity.setTitle("DISCONNECTED");
            mainActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            mainActivity.getSupportActionBar().setIcon(R.drawable.disconnected);
        }
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
        new ConnectTask().execute("");
        new ControlTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    public static class ConnectTask extends AsyncTask<String,String,TcpInterface> {

        @Override
        protected TcpInterface doInBackground(String... message) {
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


}

package erostamas.brewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import static erostamas.brewer.MainActivity.current_curve;
import static erostamas.brewer.MainActivity.mainActivity;
import static erostamas.brewer.MainActivity.segmentlistadapter;

/**
 * Created by etamero on 2016.06.30..
 */

public class DisplaySegmentsActivity extends AppCompatActivity {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String CURVE_NAME = "curve_name";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

    public DisplaySegmentsActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_segments);
        setTitle(MainActivity.current_curve);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ListView listView = (ListView) findViewById(R.id.segment_list);
        Log.i("segments", "segment activity created: " + MainActivity.curves.get(current_curve).toString());
        segmentlistadapter = new SegmentListAdapter(MainActivity.curves.get(current_curve));
        listView.setAdapter(segmentlistadapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_segments_menu, menu);
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

        if (id == R.id.add_segment) {
            Intent intent = new Intent(DisplaySegmentsActivity.this, AddSegmentActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

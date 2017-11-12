package erostamas.brewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import static erostamas.brewer.DisplayCurvesFragment.CURVE_NAME;
import static erostamas.brewer.DisplayCurvesFragment.curveListAdapter;
import static erostamas.brewer.DisplayCurvesFragment.curves;
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
    public static final String SEGMENT_INDEX = "com.example.brewer.SEGMENT_INDEX";
    public static String _curveName;
    public static SegmentListAdapter segmentListAdapter;

    public DisplaySegmentsActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_segments);
        Intent intent = getIntent();
        _curveName = intent.getStringExtra(CURVE_NAME);
        setTitle(_curveName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ListView listView = (ListView) findViewById(R.id.segment_list);
        segmentListAdapter = new SegmentListAdapter();
        listView.setAdapter(segmentListAdapter);
        int[] colors = {0, 0xFFFFFFFF, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        listView.setDividerHeight(1);
        registerForContextMenu(listView);
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DisplaySegmentsActivity.this, AddSegmentActivity.class);
                intent.putExtra(CURVE_NAME, _curveName);
                startActivity(intent);
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.segment_list){

            MenuItem mnu1=menu.add(0,0,0,"Edit");
            MenuItem mnu2=menu.add(0,1,1,"Delete");
        }
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = info.position;
        switch (item.getItemId()) {
            case 0:
                Intent intent = new Intent(DisplaySegmentsActivity.this, AddSegmentActivity.class);
                intent.putExtra(CURVE_NAME, _curveName);
                intent.putExtra(SEGMENT_INDEX, index);
                startActivity(intent);
                break;
            case 1:
                curves.get(_curveName).getSegments().remove(index);
                segmentListAdapter.notifyDataSetChanged();
                break;

            default:
                break;

        }
        return true;
    }
}

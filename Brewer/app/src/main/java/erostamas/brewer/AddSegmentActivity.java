package erostamas.brewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.ArrayList;

import static erostamas.brewer.MainActivity.current_curve;
import static erostamas.brewer.MainActivity.segmentlistadapter;

public class AddSegmentActivity extends AppCompatActivity {

    private String _curveName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_segment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        _curveName = intent.getStringExtra(DisplayCurvesFragment.CURVE_NAME);
        setTitle("Add new segment");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NumberPicker hourPicker = (NumberPicker)findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(4);

        NumberPicker minutePicker = (NumberPicker)findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.tempPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuItem add = menu.add(Menu.NONE, 0, 0, "Save");
        add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        MenuItem qkAdd = menu.add(Menu.NONE, 1, 1, "Cancel");
        qkAdd.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getTitle() == "Save") {
            NumberPicker hourPicker = (NumberPicker)findViewById(R.id.hourPicker);
            NumberPicker minutePicker = (NumberPicker)findViewById(R.id.minutePicker);
            NumberPicker tempPicker = (NumberPicker) findViewById(R.id.tempPicker);

            DisplayCurvesFragment.curves.get(_curveName).add(new Segment(tempPicker.getValue(), hourPicker.getValue(), minutePicker.getValue()));
            DisplaySegmentsActivity.segmentListAdapter.notifyDataSetChanged();
            this.finish();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getFragmentManager().popBackStack();

    }

}

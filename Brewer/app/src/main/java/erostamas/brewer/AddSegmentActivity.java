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
    private boolean _editMode = false;
    private int _segmentIndex = -1;
    private Segment _segment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_segment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NumberPicker hourPicker = (NumberPicker)findViewById(R.id.hourPicker);
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(4);

        NumberPicker minutePicker = (NumberPicker)findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        NumberPicker tempPicker = (NumberPicker) findViewById(R.id.tempPicker);
        tempPicker.setMinValue(0);
        tempPicker.setMaxValue(100);
        Intent intent = getIntent();
        _curveName = intent.getStringExtra(DisplayCurvesFragment.CURVE_NAME);
        if (intent.hasExtra(DisplaySegmentsActivity.SEGMENT_INDEX)) {
            _segmentIndex = intent.getIntExtra(DisplaySegmentsActivity.SEGMENT_INDEX, 0);
            setTitle("Add new segment");
            _editMode = true;
            _segment = DisplayCurvesFragment.curves.get(_curveName).getSegments().get(_segmentIndex);
            hourPicker.setValue(_segment.getHours());
            minutePicker.setValue(_segment.getMinutes());
            tempPicker.setValue(_segment.getTemp());
        } else {
            setTitle("Edit segment");
            _editMode = false;
        }

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
            if (_editMode) {
                _segment._hours = hourPicker.getValue();
                _segment._minutes = minutePicker.getValue();
                _segment._temp = tempPicker.getValue();
                DisplaySegmentsActivity.segmentListAdapter.notifyDataSetChanged();
            } else {
                DisplayCurvesFragment.curves.get(_curveName).add(new Segment(tempPicker.getValue(), hourPicker.getValue(), minutePicker.getValue()));
                DisplaySegmentsActivity.segmentListAdapter.notifyDataSetChanged();
            }
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

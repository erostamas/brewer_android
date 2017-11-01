package erostamas.brewer;

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

import java.util.ArrayList;

import static erostamas.brewer.MainActivity.current_curve;
import static erostamas.brewer.MainActivity.segmentlistadapter;

public class AddSegmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_segment);
        setTitle("Add new segment");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
            EditText tempText = (EditText)findViewById(R.id.set_temp_textbox);
            EditText timeText = (EditText)findViewById(R.id.set_duration_textbox);
            double temp = Double.parseDouble(tempText.getText().toString());
            long time = Long.parseLong(timeText.getText().toString());
            MainActivity.curves.get(MainActivity.current_curve).add(new Segment(temp, time));
            MainActivity.segmentlistadapter.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

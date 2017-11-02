package erostamas.brewer;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

public class AddCurveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curve);
        setTitle("Add new curve");
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
        if (item.getTitle() == "Save") {
            ArrayList<Segment> curve = new ArrayList<Segment>();
            EditText curveName = (EditText)findViewById(R.id.set_curve_name_textbox);
            DisplayCurvesFragment.curves.put(curveName.getText().toString(), curve);
            DisplayCurvesFragment.curveListAdapter.notifyDataSetChanged();
            NavUtils.navigateUpFromSameTask(this);
        }
        if (item.getTitle() == "Cancel") {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        getFragmentManager().popBackStack();

    }
}

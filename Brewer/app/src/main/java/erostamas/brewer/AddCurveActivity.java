package erostamas.brewer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;

import static erostamas.brewer.DisplayCurvesFragment.curves;

public class AddCurveActivity extends AppCompatActivity {

    private boolean _editMode = false;
    private String _curveName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_curve);
        Intent intent = getIntent();
        if (intent.hasExtra(DisplayCurvesFragment.CURVE_NAME)) {
            _curveName = intent.getStringExtra(DisplayCurvesFragment.CURVE_NAME);
            _editMode = true;
            setTitle("Edit curve " + _curveName);
            ((EditText) findViewById(R.id.set_curve_name_textbox)).setText(_curveName);
        } else {
            _curveName = "";
            _editMode = false;
            setTitle("Add new curve");
        }


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
            String newCurveName = ((EditText) findViewById(R.id.set_curve_name_textbox)).getText().toString();
            if (newCurveName.length() != 0) {
                if (!_editMode) {
                    Curve curve = new Curve(newCurveName);
                    curves.put(newCurveName, curve);
                    DisplayCurvesFragment.curveListAdapter.notifyDataSetChanged();
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    Curve curve = curves.get(_curveName);
                    curve.setName(newCurveName);
                    curves.put(newCurveName, curve);
                    curves.remove(_curveName);
                    DisplayCurvesFragment.curveListAdapter.notifyDataSetChanged();
                    NavUtils.navigateUpFromSameTask(this);
                }
            }
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

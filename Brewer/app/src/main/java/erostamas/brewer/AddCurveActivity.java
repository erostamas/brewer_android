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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static erostamas.brewer.MainActivity.current_curve;
import static erostamas.brewer.MainActivity.mainActivity;
import static erostamas.brewer.MainActivity.segmentlistadapter;

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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (item.getTitle() == "Save") {
            ArrayList<Segment> curve = new ArrayList<Segment>();
            EditText curveName = (EditText)findViewById(R.id.set_curve_name_textbox);
            MainActivity.curves.put(curveName.getText().toString(), curve);
            MainActivity.curvelistadapter.notifyDataSetChanged();
            Iterator it = MainActivity.curves.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey());
                it.remove(); // avoids a ConcurrentModificationException
            }
        }

        return super.onOptionsItemSelected(item);
    }

}

package erostamas.brewer;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static java.security.AccessController.getContext;

/**
 * Created by etamero on 2017.11.23..
 */

public class ImportCurvesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri data = getIntent().getData();
        if (data != null) {
            getIntent().setData(null);
            try {
                importData(data);
            } catch (Exception e) {
                // warn user about bad data here
                finish();
                return;
            }

            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void importData(Uri data) {
        final String scheme = data.getScheme();

        if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            try {
                ContentResolver cr = getContentResolver();
                InputStream is = cr.openInputStream(data);
                if (is == null) return;

                StringBuffer buf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                if (is != null) {
                    while ((line = reader.readLine()) != null) {
                        Curve curve = new Curve(line);
                        DisplayCurvesFragment.curves.put(curve.getName(), curve);
                    }
                }
                is.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            try {
                InputStream instream = getContentResolver().openInputStream(data);
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
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

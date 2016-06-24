package erostamas.brewer;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by etamero on 2016.04.10..
 */

public class ControlTask extends AsyncTask<Void, Void, ControlTask> {

    @Override
    protected ControlTask doInBackground(Void... params) {
        run();
        return null;
    }

    public void run() {
        while(true) {
            Log.i("Brewer", "Control Task running loop");
            MainActivity.tcpInterface.sendMessage("get_temperature");
            MainActivity.tcpInterface.sendMessage("get_setpoint");
            SystemClock.sleep(1000);
        }
    }
}


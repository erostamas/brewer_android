package erostamas.brewer;

import android.os.SystemClock;

/**
 * Created by etamero on 2016.04.10..
 */
public class ControlThread extends Thread {
    @Override
    public void run() {
        while(true) {
            MainActivity.tcpInterface.sendMessage("get_temperature");
            MainActivity.tcpInterface.sendMessage("get_setpoint");
            SystemClock.sleep(1000);
        }
    }
}

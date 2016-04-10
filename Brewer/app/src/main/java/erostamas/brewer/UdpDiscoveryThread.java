package erostamas.brewer;

import android.os.AsyncTask;

import java.net.DatagramSocket;

/**
 * Created by etamero on 2016.04.10..
 */
public class UdpDiscoveryThread extends Thread {
    @Override
    public void run() {
        try {
            MainActivity.udpSocket = new DatagramSocket();
        } catch (Exception e) {
        }
        UdpInterface udpinterface = new UdpInterface();
        while (MainActivity.brewerAddress == "") {
            if (udpinterface.getStatus() == AsyncTask.Status.FINISHED || udpinterface.getStatus() == AsyncTask.Status.PENDING){
                udpinterface.cancel(true);
                udpinterface = new UdpInterface();
                udpinterface.execute();}
        }
        MainActivity.connectionEstablished();

    }
}

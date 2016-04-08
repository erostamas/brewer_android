package erostamas.brewer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by etamero on 2016.04.08..
 */
public class UdpInterface extends AsyncTask{
    public UdpInterface(){}

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.i("Brewer", "async task udp finished");
    }

    @Override
    protected Object doInBackground(Object[] params) {
        MainActivity.setBrewerAddress(findBrewer());
        return null;
    }

    public String findBrewer() {
        try {
            String messageStr = "where are you brewer?";
            int server_port = 5001;
            MainActivity.s.setBroadcast(true);
            InetAddress bcast = InetAddress.getByName("192.168.1.101");
            int msg_length = messageStr.length();
            byte[] message = messageStr.getBytes();

            DatagramPacket p = new DatagramPacket(message, msg_length, bcast, server_port);
            MainActivity.s.send(p);//properly able to send data. i receive data to server
            Log.i("Brewer", "sent data");

            final byte[] receiveData = new byte[1024];
            Log.d("Brewer", "Waiting for Broadcast request in ServerUDP.");
            final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            MainActivity.s.setSoTimeout(2000);
            MainActivity.s.receive(receivePacket);
            String req = new String(receivePacket.getData(), 0, receivePacket.getLength());
            Log.d("Brewer", "Received UDP message : "+req+" from: "+receivePacket.getAddress().getHostAddress());
            return req;

        }
        catch (Exception e) {
            Log.i("Brewer", "something went wrong with udp sending");
            Log.d("Brewer", e.getStackTrace().toString());
            return "";
        }

    }
}

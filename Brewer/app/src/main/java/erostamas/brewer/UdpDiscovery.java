package erostamas.brewer;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static erostamas.brewer.ControlFragment.restartDownloadTask;
import static erostamas.brewer.MainActivity.mainActivity;

/**
 * Created by etamero on 2017.09.10..
 */

public class UdpDiscovery extends AsyncTask<UdpMessage, Long, String> {
    private Context mContext;
    public static boolean successfulDiscovery = false;
    public static boolean finished = true;

    public UdpDiscovery(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(UdpMessage... udpMessages) {
        try {
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            int server_port = udpMessages[0].getPort();
            DatagramSocket s = new DatagramSocket();
            s.setBroadcast(true);
            //InetAddress local = InetAddress.getByName();
            int msg_length = udpMessages[0].getMessage().length();
            byte[] message = udpMessages[0].getMessage().getBytes();
            DatagramPacket p = new DatagramPacket(message, msg_length, getBroadcastAddress(), server_port);
            s.send(p);
            Log.i("UDP", "UDP packet sent");
            s.setSoTimeout(1000);
            s.receive(packet);
            String lText = new String(buffer, 0, packet.getLength());
            Log.i("UDP", "UDP packet received: " + lText + "from: " + packet.getAddress().getHostAddress());
            return packet.getAddress().getHostAddress().toString();
        } catch (Exception e) {
            Log.e("brewer", "Exception during UDP send: " + e.getMessage());
        }
        return null;
    }

    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        successfulDiscovery = false;
        finished = false;
    }

    @Override
    protected void onPostExecute(String brewerAddress) {
        if (brewerAddress != null) {
            mainActivity.brewerAddress = brewerAddress;
            restartDownloadTask();
            successfulDiscovery = true;
        }
        finished = true;
    }
}

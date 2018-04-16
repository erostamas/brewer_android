package erostamas.brewer;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import redis.clients.jedis.Jedis;

import static erostamas.brewer.MainActivity.mainActivity;
import static erostamas.brewer.MainActivity.myappcontext;


/**
 * Created by etamero on 2017.09.06..
 */

public class BrewerDataDownloader extends AsyncTask <Void, Integer, ProcessData> {

    private Context mContext;

    public BrewerDataDownloader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ProcessData doInBackground(Void... params) {
        ProcessData ret = new ProcessData();
        try {
            Jedis jedis = new Jedis(BrewerData.brewerIpAddress, 6379);
            jedis.connect();
            double temp = Double.parseDouble(jedis.hmget("brewer_process_data", "temp").get(0));
            ret.setTemperature(temp);
            int setpoint = Integer.parseInt(jedis.hmget("brewer_process_data", "setpoint").get(0));
            ret.setSetpoint(setpoint);
            //int nextsetpoint = Integer.parseInt(jedis.hmget("brewer_process_data", "nextsetpoint").get(0));
            //ret.setNextSetpoint(nextsetpoint);
            int output = Integer.parseInt(jedis.hmget("brewer_process_data", "output").get(0));
            ret.setOutput(output);
            String mode = jedis.hmget("brewer_process_data", "mode").get(0);
            ret.setMode(mode);
            String curve = jedis.hmget("brewer_process_data", "current_curve").get(0);
            ret.setCurrentCurveName(curve);
            int timetonextsegment = Integer.parseInt(jedis.hmget("brewer_process_data", "time_to_next_segment").get(0));
            ret.setTimeToNextSetpoint(timetonextsegment);

        } catch (Exception e) {
            Log.e("brewer", "Exception during accessing redis: " + e.getMessage());
            while(!discovery()) {
            }
        }
        ret.validate();
        return ret;
    }

    @Override
    protected void onPostExecute(ProcessData processData) {
        if (processData.isValid()){
            BrewerData.temperature = processData.getTemperature();
            BrewerData.setpoint = processData.getSetpoint();
            BrewerData.output = processData.getOutput();
            BrewerData.nextSetpoint = processData.getNextSetpoint();
            BrewerData.timeToNextSetpoint = processData.getTimeToNextSetpoint();
            BrewerData.mode = processData.getMode();
            BrewerData.currentCurveName = processData.getCurrentCurveName();
        } else {
            Log.i("discovery", "redis stuff failed...");
            while(!discovery()) {
            }
        }
    }

    boolean discovery() {
        try {
            byte[] buffer = new byte[2048];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            int server_port = 50001;
            DatagramSocket s = new DatagramSocket();
            s.setBroadcast(true);
            String message = "where are you brewer?";
            //InetAddress local = InetAddress.getByName();
            int msg_length = message.length();
            byte[] messageBytes = message.getBytes();
            DatagramPacket p = new DatagramPacket(messageBytes, msg_length, getBroadcastAddress(), server_port);
            s.send(p);
            Log.i("UDP", "UDP packet sent");
            s.setSoTimeout(1000);
            s.receive(packet);
            String lText = new String(buffer, 0, packet.getLength());
            Log.i("UDP", "UDP packet received: " + lText + "from: " + packet.getAddress().getHostAddress());
            BrewerData.brewerIpAddress = packet.getAddress().getHostAddress().toString();
            return true;
        } catch (Exception e) {
            Log.e("brewer", "Exception during UDP send: " + e.getMessage());
            return false;
        }
    }

    InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifi = (WifiManager)myappcontext.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        // handle null somehow

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }
}

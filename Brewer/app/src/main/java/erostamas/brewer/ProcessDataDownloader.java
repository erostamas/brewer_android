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

import static erostamas.brewer.MainActivity.mainActivity;
import static erostamas.brewer.MainActivity.myappcontext;


/**
 * Created by etamero on 2017.09.06..
 */

public class ProcessDataDownloader extends AsyncTask <String, Integer, ProcessData> {

    private Context mContext;

    public ProcessDataDownloader(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected ProcessData doInBackground(String... urls) {
        ProcessData ret = new ProcessData();
        try {
            URL url = new URL("http://" + mainActivity.brewerAddress + "/process_data.php");
            Log.i("brewer", "downloading xml at: " + urls[0]);
            //URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            StringBuilder buf = new StringBuilder(512);
            String line;
            while((line=reader.readLine())!=null){
                buf.append(line).append("\n");
            }
            urlConnection.disconnect();
            Log.i("brewer", "downloaded: " + buf.toString());
            InputStream stream = new ByteArrayInputStream(buf.toString().getBytes(StandardCharsets.UTF_8.name()));
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(stream, null);
                parser.nextTag();
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    if (name.equals("temp")) {
                        ret.setTemperature(Double.parseDouble(readText(parser)));
                        Log.i("brewer", "temp found: " + ret.getTemperature());
                    } else if (name.equals("setpoint")) {
                        ret.setSetpoint(Integer.parseInt(readText(parser)));
                    } else if (name.equals("mode")) {
                        ret.setMode(readText(parser));
                    } else if (name.equals("output")) {
                        ret.setOutput(Integer.parseInt(readText(parser)));
                    } else if (name.equals("nextsetpoint")) {
                        ret.setNextSetpoint(Integer.parseInt(readText(parser)));
                    } else if (name.equals("time_to_next_segment")) {
                        ret.setTimeToNextSetpoint(Integer.parseInt(readText(parser)));
                    } else if (name.equals("current_curve")) {
                        ret.setCurrentCurveName(readText(parser));
                    } else {
                        skip(parser);
                    }
                }

            } finally {
                stream.close();
                ret.validate();
            }
        } catch (Exception e) {
            Log.e("brewer", "Exception during download of data xml: " + e.getMessage());
            while(!discovery()) {
            }
        }
        return ret;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    protected void onPostExecute(ProcessData processData) {
        if (processData.isValid()) {
            Log.i("discovery", "Download ok");
            mainActivity.currentTemperature = processData.getTemperature();
            ControlFragment.currentMode = processData.getMode();
            mainActivity.currentOutput = processData.getOutput();
            mainActivity.currentSetpoint = processData.getSetpoint();
            mainActivity.nextSetpoint = processData.getNextSetpoint();
            mainActivity.timeToNextSetpoint = processData.getTimeToNextSetpoint();
            ControlFragment.currentCurveName = processData.getCurrentCurveName();
            mainActivity.updateUI();
        } else {
            Log.i("discovery", "Download failed, starting brewer discovery...");
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
            MainActivity.brewerAddress = packet.getAddress().getHostAddress().toString();
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

package erostamas.brewer;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static erostamas.brewer.MainActivity.mainActivity;


/**
 * Created by etamero on 2017.09.06..
 */

public class ProcessDataDownloader extends AsyncTask <String, Integer, Long> {

    Double _temp;

    @Override
    protected Long doInBackground(String... urls) {
        try {
            Log.i("brewer", "downloading xml at: " + urls[0]);
            URL url = new URL(urls[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
            StringBuilder buf = new StringBuilder(512);
            String line = null;
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
                        _temp = Double.parseDouble(readText(parser));
                        Log.i("brewer", "temp: " + Double.toString(_temp));
                    } else {
                        skip(parser);
                    }
                }

            } finally {
                stream.close();
            }
        } catch (Exception e) {
            Log.e("brewer", "Exception during download of data xml: " + e.getMessage());
        }
        return null;
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
    protected void onPostExecute(Long aLong) {
        mainActivity.currentTemperature = _temp;
        mainActivity.updateUI();
    }
}

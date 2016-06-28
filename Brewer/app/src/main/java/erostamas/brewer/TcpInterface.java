package erostamas.brewer;

import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Time;

import static erostamas.brewer.MainActivity.mainActivity;

/**
 * Created by etamero on 2016.04.10..
 */
public class TcpInterface {
    private String serverMessage;
    private OnMessageReceived mMessageListener = null;
    private boolean mRun = false;
    public boolean connected = false;
    private long msg_recieved = 0;
    PrintWriter out;
    BufferedReader in;
    Socket socket;

    /**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TcpInterface(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        Log.i("Brewer", "sendmessage called");
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }

    public void stopClient(){
        mRun = false;
    }

    public void connect() {
        try {
            Log.i("timing", "connect called");
            InetAddress serverAddr = InetAddress.getByName(MainActivity.brewerAddress);

            Log.e("TCP Client", "C: Connecting...");

            //create a socket to make the connection with the server
            if (socket != null) {
                socket.close();
                socket = null;
            }
            socket = new Socket(serverAddr, 5000);
            Log.i("timing", "connected");
            connected = true;
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.updateUI();
                }
            });
            //send the message to the server
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            //receive the message which the server sends back
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            msg_recieved = System.currentTimeMillis();
            sendMessage("get_curves");

        } catch (IOException e){
            Log.e("TCP", "C: Error", e);
        }
    }
    public void disconnect() {
        connected = false;
        if (socket != null) {
            try {
                socket.close();
                socket = null;
            } catch (Exception e) {

            }
            }
    }

    public void run() {

        mRun = true;
        Log.i("Brewer", "Tcp interface running");
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.updateUI();
            }
        });
        try {
            //in this while the client listens for the messages sent by the server
            while (mRun) {
                Log.i("timing", "jytdfjtd");
                Log.i("timing", "msg recieved = " + msg_recieved);
                Log.i("timing", "now = " + System.currentTimeMillis());
                if (!connected || (System.currentTimeMillis() - msg_recieved > 3000)) {
                    disconnect();
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.updateUI();
                        }
                    });
                    connect();
                } else {
                    try {
                        serverMessage = in.readLine();
                    } catch (Exception e) {
                    }
                    Log.i("Brewer", "Server read tried");
                    if (serverMessage != null && mMessageListener != null) {
                        msg_recieved = System.currentTimeMillis();
                        Log.i("brewer", Long.toString(msg_recieved));
                        Log.i("Brewer", "server read");
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                        Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
                    }
                    serverMessage = null;

                }

            }

            } catch (Exception e) {

                Log.e("TCP", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                try {
                    disconnect();
                    mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.updateUI();
                        }
                    });
                } catch (Exception e) {
                }
            }

            }


    //Declare the interface. The method messageReceived(String message) will must be implemented in the MyActivity
    //class at on asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}


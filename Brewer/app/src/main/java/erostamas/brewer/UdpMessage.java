package erostamas.brewer;

/**
 * Created by etamero on 2017.09.10..
 */

public class UdpMessage {

    UdpMessage(String address, int port, String message) {
        _address = address;
        _port = port;
        _message = message;
    }

    String getAddress() { return _address; }
    int getPort() {return _port;}
    String getMessage() {return _message;}

    private int _port;
    private String _address;
    private String _message;
}

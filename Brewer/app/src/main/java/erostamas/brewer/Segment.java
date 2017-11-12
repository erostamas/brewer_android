package erostamas.brewer;

/**
 * Created by etamero on 2016.06.30..
 */

public class Segment {

    int _temp = 0;
    int _hours = 0;
    int _minutes = 0;

    Segment(int temp, int hours, int minutes) {
        _temp = temp;
        _hours = hours;
        _minutes = minutes;
    }

    Segment(String segmentString) {
        String[] separated = segmentString.split(":");
        _temp = Integer.parseInt(separated[0]);
        int seconds = Integer.parseInt(separated[1]);
        _hours = seconds / 3600;
        _minutes = (seconds - _hours * 3600) / 60;
    }

    @Override
    public String toString() {
        return _temp + ":" + Long.toString(_hours * 3600 + _minutes * 60);
    }

    public int getHours() { return _hours;}
    public int getMinutes() {return _minutes;}
    public int getTemp() {return _temp;}
}

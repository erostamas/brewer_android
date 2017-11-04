package erostamas.brewer;

/**
 * Created by etamero on 2016.06.30..
 */

public class Segment {

    double _temp = 0.0;
    long _hours = 0;
    long _minutes = 0;
    Segment(double temp, long hours, long minutes) {
        _temp = temp;
        _hours = hours;
        _minutes = minutes;
    }
}

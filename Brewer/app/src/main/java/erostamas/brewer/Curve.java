package erostamas.brewer;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by etamero on 2017.11.05..
 */

public class Curve {
    Curve() {}
    Curve(String curveString) {
        Log.i("brewer_file", "CUrve init from string: " + curveString);
        String[] separated = curveString.split(" ");
        _name = separated[0];
        if (separated.length > 1) {
            setSegments(separated[1]);
        }
    }

    Curve(String name, String curveString) {
        _name = name;
        setSegments(curveString);
    }

    public void setName(String name) {
        _name = name;
    }

    private void setSegments(String curveString) {
        String[] segments = curveString.split(";");
        for (int i = 0; i < segments.length; i++) {
            Log.i("brewer_file", "Segment created from string: " + segments[i]);
            Segment s = new Segment(segments[i]);
            _segments.add(s);
        }
    }

    public void add(Segment s) {
        _segments.add(s);
    }

    public ArrayList<Segment> getSegments() {
        return _segments;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String toString() {
        String ret = _name + " ";
        for (int i = 0; i < _segments.size(); i++) {
            if (i != 0) {
                ret += ";";
            }
            ret += _segments.get(i).toString();
        }
        return ret;
    }

    private String _name;
    private ArrayList<Segment> _segments = new ArrayList<>();
}

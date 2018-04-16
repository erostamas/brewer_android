package erostamas.brewer;

/**
 * Created by etamero on 2017.09.07..
 */

public class ProcessData {
    public void setTemperature (double temperature) {
        _temperature = temperature;
    }
    public void setMode(String mode) {
        _mode = mode;
    }
    public void setOutput(int output) {
        _output = output;
    }
    public void setSetpoint(int setpoint) { _setpoint = setpoint; }
    public void setNextSetpoint(int nextSetpoint) { _nextSetpoint = nextSetpoint; }
    public void setTimeToNextSetpoint(int time) { _timeToNextSetpoint = time; }
    public void setCurrentCurveName(String curveName) { _currentCurveName = curveName; }
    public void validate() { _valid = true; }

    public double getTemperature() {
        return _temperature;
    }

    public String getMode() {
        return _mode;
    }
    public int getOutput() {
        return _output;
    }
    public int getSetpoint() {
        return _setpoint;
    }
    public int getNextSetpoint() {
        return _nextSetpoint;
    }
    public int getTimeToNextSetpoint() {
        return _timeToNextSetpoint;
    }
    public String getCurrentCurveName() {
        return _currentCurveName;
    }
    public boolean isValid() { return _valid; }

    private double _temperature = -1.0;
    private String _mode = "";
    private int _output = 0;
    private int _setpoint = 0;
    private int _nextSetpoint = 0;
    private int _timeToNextSetpoint = 0;
    private String _currentCurveName = "";
    private boolean _valid = false;
}

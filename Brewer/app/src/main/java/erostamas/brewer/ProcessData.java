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
    public void setSetpoint(double setpoint) { _setpoint = setpoint; }
    public void setNextSetpoint(double nextSetpoint) { _nextSetpoint = nextSetpoint; }
    public void setTimeToNextSetpoint(int time) { _timeToNextSetpoint = time; }

    public double getTemperature() {
        return _temperature;
    }

    public String getMode() {
        return _mode;
    }
    public int getOutput() {
        return _output;
    }
    public double getSetpoint() {
        return _setpoint;
    }
    public double getNextSetpoint() {
        return _nextSetpoint;
    }
    public int getTimeToNextSetpoint() {
        return _timeToNextSetpoint;
    }

    private double _temperature = 0.0;
    private String _mode = "";
    private int _output = 0;
    private double _setpoint = 0.0;
    private double _nextSetpoint = 0.0;
    private int _timeToNextSetpoint = 0;
}

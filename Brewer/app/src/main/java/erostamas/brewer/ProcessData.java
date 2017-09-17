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
    private double _temperature;
    private String _mode;
    private int _output;
    private double _setpoint;
}

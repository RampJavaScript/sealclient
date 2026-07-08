package client;

import java.util.List;

public class Setting {
    private String name;
    private Module parent;
    private String type; // "BOOLEAN", "NUMBER", "MODE"
    
    private boolean bVal;
    private double nVal, min, max;
    private String sVal;
    private List<String> modes;
    private boolean isRainbow;

    // Mode Setting Constructor
    public Setting(String name, Module parent, String sVal, List<String> modes) {
        this.name = name; this.parent = parent; this.sVal = sVal; this.modes = modes; this.type = "MODE";
    }
    // Number Setting Constructor
    public Setting(String name, Module parent, double nVal, double min, double max) {
        this.name = name; this.parent = parent; this.nVal = nVal; this.min = min; this.max = max; this.type = "NUMBER";
    }
    // Boolean Setting Constructor
    public Setting(String name, Module parent, boolean isRainbow) {
        this.name = name; this.parent = parent; this.isRainbow = isRainbow; this.type = "BOOLEAN";
    }

    public String getName() { return name; }
    public Module getParent() { return parent; }
    public String getType() { return type; }
    public boolean getValBoolean() { return bVal; }
    public double getValDouble() { return nVal; }
    public void setValDouble(double val) { this.nVal = val; }
    public String getValMode() { return sVal; }
    public void setValMode(String sVal) { this.sVal = sVal; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public boolean isRainbow() { return isRainbow; }
    public void setRainbow(boolean rainbow) { this.isRainbow = rainbow; }
}

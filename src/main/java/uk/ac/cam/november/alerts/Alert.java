package uk.ac.cam.november.alerts;

/**
 * 
 * @author Alan Tang
 * 
 * My proposal for an alert system. Contains a type, a value, as well as possible addition fields.
 * The Alert Generator will produce something of this type when it detects an issue with the measurements.
 * It will then send it to the MessageFormatter.
 *
 */
public class Alert {
    private String mAlertType;
    private double mValue;
    //Possibly additional fields
    
    public Alert(String alertType, double value)
    {
        mAlertType = alertType;
        mValue = value;
    }

    public String getAlertType() {
        return mAlertType;
    }
    public double getValue() {
        return mValue;
    }
}

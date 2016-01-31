package uk.ac.cam.november.alerts;

/**
 * 
 * @author Alan Tang
 * 
 * My proposal for an alert system
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

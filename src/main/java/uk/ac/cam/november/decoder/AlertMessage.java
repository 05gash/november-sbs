package uk.ac.cam.november.decoder;

public class AlertMessage {
    
    /** Type of an Alert Message
     * 0 - CriticalChange alert
     * 1 - CriticalMax value alert
     * 2 - CriticalMin value alert
     * 3 - TimeOut alert
     */  
    private int alertType;
    
    
    /** Type of Sensor that provoked an AlertMessage
     * 0 - WaterDepth
     * 1 - WindSpeed
     * 2 - WindAngle
     * 3 - BoatHeading
     * 4 - BoatSpeed
     */ 
    private int sensor;

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public int getSensor() {
        return sensor;
    }

    public void setSensor(int sensor) {
        this.sensor = sensor;
    }

}

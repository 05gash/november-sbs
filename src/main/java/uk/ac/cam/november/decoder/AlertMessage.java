package uk.ac.cam.november.decoder;

public class AlertMessage {

    private int alertType; // 0 - change; 1 - max value, 2- min value
    private int sensor; // 0 - WaterDepth; 1 - WindSpeed; 2 - WindAngle; 3 - BoatHeading;
                      // 4 - BoatSpeed

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

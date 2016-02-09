package uk.ac.cam.november.alerts;

public class AlertMessage {

    private int type; // 0 - change; 1 - value
    private int what; // 0 - WD; 1 - WindSpeed; 2 - WindAngle; 3 - BoatHeading;
                      // 4 - BoatSpeed

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

}

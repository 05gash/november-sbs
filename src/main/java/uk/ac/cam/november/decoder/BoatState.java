package uk.ac.cam.november.decoder;

/**
 * This class defines a Boat State. It's attributes come from sensors: 
 * Water Depth sensor, Wind Data sensor, Boat Heading sensor, and Boat Speed sensor.
 * For every type of data, the class initializes appropriate get/set methods.
 * 
 * @author Marie Menshova
 *
 */

public class BoatState {
    
    private int SID;
    private float Depth;
    private float Offset;
    private float WindSpeed;
    private float WindAngle;
    private float SpeedWaterReferenced;
    private float Heading;
    private float Deviation;
    private float Variation;
    private float Latitude;
    private float Longtitude;
    private float Altitude;
    
    public int getSID() {
        return SID;
    }
    public void setSID(int sID) {
        SID = sID;
    }
    public float getDepth() {
        return Depth;
    }
    public void setDepth(float depth) {
        Depth = depth;
    }
    public float getOffset() {
        return Offset;
    }
    public void setOffset(float offset) {
        Offset = offset;
    }
    public float getWindSpeed() {
        return WindSpeed;
    }
    public void setWindSpeed(float windSpeed) {
        WindSpeed = windSpeed;
    }
    public float getWindAngle() {
        return WindAngle;
    }
    public void setWindAngle(float windAngle) {
        WindAngle = windAngle;
    }
    public float getSpeedWaterReferenced() {
        return SpeedWaterReferenced;
    }
    public void setSpeedWaterReferenced(float speedWaterReferenced) {
        SpeedWaterReferenced = speedWaterReferenced;
    }
    public float getHeading() {
        return Heading;
    }
    public void setHeading(float heading) {
        Heading = heading;
    }
    public float getDeviation() {
        return Deviation;
    }
    public void setDeviation(float deviation) {
        Deviation = deviation;
    }
    public float getVariation() {
        return Variation;
    }
    public void setVariation(float variation) {
        Variation = variation;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(float longtitude) {
        Longtitude = longtitude;
    }

    public float getAltitude() {
        return Altitude;
    }

    public void setAltitude(float altitude) {
        Altitude = altitude;
    }
}

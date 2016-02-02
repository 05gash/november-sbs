package uk.ac.cam.november.message;

import com.google.gson.annotations.SerializedName;

public class Fields {
    private int SID;
    private float Depth;
    private float Offset;
    @SerializedName("Wind Speed")
    private float WindSpeed;
    @SerializedName("Wind Angle")
    private float WindAngle;
    @SerializedName("Speed Water Referenced")
    private float SpeedWaterReferenced;
    private float Heading;
    private float Deviation;
    private float Variation;
    
    
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
}

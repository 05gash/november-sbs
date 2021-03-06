package uk.ac.cam.november.packet;

import com.google.gson.annotations.SerializedName;

/**
 * This is a generic Fields class - take note some fields may be null because of the
 * way GSON handles deserialization The container message should give
 * information on which fields are filled in, for example a message with
 * description Heading Data will have fields Heading, Deviation and
 * 
 * @author George Ash
 * 
 */

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
    private String SpeedWaterReferencedType;
    private float Heading;
    private float Deviation;
    private float Variation;
    private String Reference;
    private float Latitude;
    private float Longtitude;
    private float Altitude;


    public int getSID() {
        return SID;
    }

    public void setSID(int sID) {
        SID = sID;
    }

    /**
     * requires pgn of container message to be 128267
     * 
     * @return
     */

    public float getDepth() {
        return Depth;
    }

    public void setDepth(float depth) {
        Depth = depth;
    }

    /**
     * requires pgn of container message to be 128267
     * 
     * @return
     */

    public float getOffset() {
        return Offset;
    }

    public void setOffset(float offset) {
        Offset = offset;
    }

    /**
     * requires pgn of container message to be 130306
     * 
     * @return
     */
    public float getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        WindSpeed = windSpeed;
    }

    /**
     * requires pgn of container message to be 130306
     * 
     * @return
     */
    public float getWindAngle() {
        return WindAngle;
    }

    public void setWindAngle(float windAngle) {
        WindAngle = windAngle;
    }

    /**
     * will return a non-null value in the case container pgn is 128259
     * 
     * @return
     */
    public float getSpeedWaterReferenced() {
        return SpeedWaterReferenced;
    }

    public void setSpeedWaterReferenced(float speedWaterReferenced) {
        SpeedWaterReferenced = speedWaterReferenced;
    }

    /**
     * will return the type of water-referenced used - non null in the case that
     * the container message has pgn 128259
     * 
     * @return
     */
    public String getSpeedWaterReferencedType() {
        return SpeedWaterReferencedType;
    }

    public void setSpeedWaterReferencedType(String speedWaterReferencedType) {
        SpeedWaterReferencedType = speedWaterReferencedType;
    }

    /**
     * will return heading - non null in the case that the container message has
     * pgn of 127250
     * 
     * @return
     */
    public float getHeading() {
        return Heading;
    }

    public void setHeading(float heading) {
        Heading = heading;
    }

    /**
     * will return deviation - non null in the case that the container message
     * has pgn of 127250
     * 
     * @return
     */
    public float getDeviation() {
        return Deviation;
    }

    public void setDeviation(float deviation) {
        Deviation = deviation;
    }

    /**
     * will return variation - non null in the case that the container message
     * has pgn of 127250
     * 
     * @return
     */
    public float getVariation() {
        return Variation;
    }

    public void setVariation(float variation) {
        Variation = variation;
    }

    /**
     * will return reference - non null in the case that the container message
     * has pgn of 127250 or 130306
     * 
     * @return
     */
    public String getReference() {
        return Reference;
    }

    public void setReference(String reference) {
        Reference = reference;
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

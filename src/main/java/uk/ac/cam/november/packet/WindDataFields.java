package uk.ac.cam.november.packet;

import com.google.gson.annotations.SerializedName;

public class WindDataFields extends Fields {
    @SerializedName("Wind Speed")
    private float WindSpeed;
    @SerializedName("Wind Angle")
    private float WindAngle;
    private String Reference;

    public float getWindSpeed() {
        return WindSpeed;
    }

    public float getWindAngle() {
        return WindAngle;
    }
    
    public String getReference() {
        return Reference;
    }

    public void setWindSpeed(float speed) {
        this.WindSpeed = speed;
    }

    public void setWindAngle(float angle) {
        this.WindAngle = angle;
    }
    
    public void setReference(String reference){
        this.Reference = reference;
    }
}

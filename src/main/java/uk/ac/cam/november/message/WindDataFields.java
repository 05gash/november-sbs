package uk.ac.cam.november.message;

import com.google.gson.annotations.SerializedName;

public class WindDataFields extends Fields {
    @SerializedName("Wind Speed")
    private float WindSpeed;
    @SerializedName("Wind Angle")
    private float WindAngle;
    
    public float getWindSpeed() {
        return WindSpeed;
    }
    public float getWindAngle() {
        return WindAngle;
    }
}

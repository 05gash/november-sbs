package uk.ac.cam.november.message;

import com.google.gson.annotations.SerializedName;

public class BoatSpeedFields extends Fields {
    @SerializedName("Speed Water Referenced")
    private float SpeedWaterReferenced;
    @SerializedName("Speed Water Referenced Type")
    private String SpeedWaterReferencedType;

    public float getSpeedWaterReferenced() {
        return SpeedWaterReferenced;
    }
    
    public String getSpeedWaterReferencedType() {
        return SpeedWaterReferencedType;
    }
    
    public void setSpeedWaterReferenced(float speed) {
        this.SpeedWaterReferenced = speed;
    }
    
    public void setSpeedWaterReferencedType(String reference) {
        this.SpeedWaterReferencedType = reference;
    }

}

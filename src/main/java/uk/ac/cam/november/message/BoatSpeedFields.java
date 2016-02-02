package uk.ac.cam.november.message;

import com.google.gson.annotations.SerializedName;

public class BoatSpeedFields extends Fields{
    @SerializedName("Speed Water Referenced")
    private float SpeedWaterReferenced;

    public float getSpeedWaterReferenced() {
        return SpeedWaterReferenced;
    }

}

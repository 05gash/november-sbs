package uk.ac.cam.november.message;

import com.google.gson.annotations.SerializedName;

public class BoatSpeedFields {
    @SerializedName("Speed Water Referenced")
    private float SpeedWaterReferenced;

    public float getSpeedWaterReferenced() {
        return SpeedWaterReferenced;
    }

}

package uk.ac.cam.november.packet;

public class WaterDepthFields extends Fields{
    private float Depth;
    private float Offset;
    
    public float getDepth() {
        return Depth;
    }
    public float getOffset() {
        return Offset;
    }
    
    public void setDepth(float depth){
        this.Depth = depth;
    }
    public void setOffset(float offset){
        this.Offset = offset;
    }
}

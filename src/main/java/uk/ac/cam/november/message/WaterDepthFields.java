package uk.ac.cam.november.message;

public class WaterDepthFields extends Fields{
    private float Depth;
    private float Offset;
    
    public void setDepth(float depth) {
        Depth = depth;
    }
    
    public void setOffset(float offset) {
        Offset = offset;
    }
    
    public float getDepth() {
        return Depth;
    }
    
    public float getOffset() {
        return Offset;
    }
}

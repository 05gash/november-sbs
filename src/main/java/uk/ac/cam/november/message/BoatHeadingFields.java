package uk.ac.cam.november.message;

public class BoatHeadingFields extends Fields {
    private float Heading;
    private float Deviation;
    private float Variation;
    private String Reference;
    
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
    
    public String getReference() {
        return this.Reference;
    }
    public void setReference(String reference) {
        this.Reference = reference;
    }
    

}

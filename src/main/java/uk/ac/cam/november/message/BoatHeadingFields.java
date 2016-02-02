package uk.ac.cam.november.message;

public class BoatHeadingFields extends Fields{
    private float Heading;
    private float Deviation;
    private float Variation;
 
    
    public float getHeading() {
       return Heading;
    }
    public float getDeviation() {
        return Deviation;
    }
    public float getVariation() {
        return Variation;
    }
    
    @Override
    public String toString(){
        return "Heading: " + Heading + " Deviation " + Deviation + " Variation " + Variation;
    }

}

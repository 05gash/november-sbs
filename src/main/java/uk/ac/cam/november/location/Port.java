package uk.ac.cam.november.location;

public class Port {

    public String name;
    public LatLng location;
    
    @Override
    public String toString(){
        return "Port: [\"" + name + "\", " + location + "]";
    }
}

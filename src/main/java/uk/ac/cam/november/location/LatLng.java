package uk.ac.cam.november.location;

public class LatLng {

    private double lat, lng;

    public LatLng(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @Override
    public String toString() {
        return "LatLng: [" + lat + ", " + lng + "]";
    }

}

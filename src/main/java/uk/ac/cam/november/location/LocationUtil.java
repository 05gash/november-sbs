package uk.ac.cam.november.location;

import java.io.InputStreamReader;

import com.google.gson.Gson;

public class LocationUtil {

    private static Port[] ports;

    static {
        PortList pl = new Gson().fromJson(new InputStreamReader(LocationUtil.class.getResourceAsStream("/ports.json")),
                PortList.class);
        ports = pl.ports;
    }

    /**
     * Calculate the initial bearing for a straight-line route from {@code from}
     * to {@code to}. Uses the 'Bearing' algorithm from
     * http://www.movable-type.co.uk/scripts/latlong.html
     * 
     * @param from
     *            Starting location
     * @param to
     *            Final location
     * @return Heading to start course at (degrees)
     */
    public static double initialBearing(LatLng from, LatLng to) {

        double lat1 = from.getLat();
        double lon1 = from.getLng();
        double lat2 = to.getLat();
        double lon2 = to.getLng();

        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double λ1 = Math.toRadians(lon1);
        double λ2 = Math.toRadians(lon2);

        double y = Math.sin(λ2 - λ1) * Math.cos(φ2);
        double x = Math.cos(φ1) * Math.sin(φ2) - Math.sin(φ1) * Math.cos(φ2) * Math.cos(λ2 - λ1);
        double brng = Math.toDegrees(Math.atan2(y, x));

        while(brng < 0.0){
            brng += 360.0;
        }
        
        while(brng > 360.0){
            brng -= 360.0;
        }
        
        return brng;
    }

    /**
     * Calculates the straight-line distance from {@code from} to {@code to}.
     * Uses the haversine formula from
     * http://www.movable-type.co.uk/scripts/latlong.html
     * 
     * @param from
     *            Starting location
     * @param to
     *            Final location
     * @return Straight-line distance between points (meters)
     */
    public static double distance(LatLng from, LatLng to) {

        double lat1 = from.getLat();
        double lon1 = from.getLng();
        double lat2 = to.getLat();
        double lon2 = to.getLng();

        double R = 6371000; // metres
        double φ1 = Math.toRadians(lat1);
        double φ2 = Math.toRadians(lat2);
        double Δφ = Math.toRadians(lat2 - lat1);
        double Δλ = Math.toRadians(lon2 - lon1);

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2)
                + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double d = R * c;

        return d;
    }

    public static Port nearestPort(LatLng loc) {
        double mindist = Double.MAX_VALUE;
        Port minport = ports[0];
        for (Port p : ports) {
            double d = distance(p.location, loc);
            if (d < mindist) {
                mindist = d;
                minport = p;
            }
        }
        return minport;
    }

}

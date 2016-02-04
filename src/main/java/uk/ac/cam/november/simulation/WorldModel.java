package uk.ac.cam.november.simulation;

/**
 * This class stores the world state used in the simulation. The values stored
 * are heading, boat speed, wind speed, wind direction and water depth.
 * 
 * @author Jamie Wood
 *
 */
public class WorldModel {

    public static final float MAX_BOAT_SPEED = 25;
    public static final float MAX_WIND_SPEED = 100;
    public static final float MAX_WATER_DEPTH = 250;

    private float heading = 0f;
    private float boatSpeed = 0f;
    private float windSpeed = 0f;
    private float windAngle = 0f;
    private float waterDepth = 0f;

    private float boatX;
    private float boatY;

    /**
     * Set the heading of the boat in the world model.
     * 
     * @param h
     *            Compass heading of the boat in degrees. Value is wrapped into
     *            the range 0-360.
     */
    public void setHeading(float h) {
        while (h < 0) {
            h += 360;
        }
        while (h > 360) {
            h -= 360;
        }
        heading = h;
    }

    /**
     * Get the current heading of the boat in the world model.
     * 
     * @return Current compass heading in degrees, in the range 0-360.
     */
    public float getHeading() {
        return heading;
    }

    /**
     * Set the boat speed in the world model.
     * 
     * @param s
     *            Boat speed in meters-per-second. Value is truncated to the
     *            range 0-{@value #MAX_BOAT_SPEED}.
     */
    public void setBoatSpeed(float s) {
        if (s < 0) {
            s = 0;
        }
        if (s > MAX_BOAT_SPEED) {
            s = MAX_BOAT_SPEED;
        }
        boatSpeed = s;
    }

    /**
     * Get the current boat speed in the world model.
     * 
     * @return Current boat speed in meters-per-second.
     */
    public float getBoatSpeed() {
        return boatSpeed;
    }

    /**
     * Set the wind speed in the world model.
     * 
     * @param s
     *            Wind speed in meters-per-second. Value is truncated into the
     *            range 0-{@value #MAX_WIND_SPEED}.
     */
    public void setWindSpeed(float s) {
        if (s < 0) {
            s = 0;
        }
        if (s > MAX_WIND_SPEED) {
            s = MAX_WIND_SPEED;
        }
        windSpeed = s;
    }

    /**
     * Get the current wind speed in the world model.
     * 
     * @return Current wind speed in meters-per-second.
     */
    public float getWindSpeed() {
        return windSpeed;
    }

    /**
     * Set the wind angle in the world model.
     * 
     * @param a
     *            Wind angle in degrees. Value is wrapped into the range 0-360.
     */
    public void setWindAngle(float a) {
        while (a < 0) {
            a += 360;
        }
        while (a > 360) {
            a -= 360;
        }
        windAngle = a;
    }

    /**
     * Get the current wind angle in the world model.
     * 
     * @return Current wind angle in degrees, in the range 0-360.
     */
    public float getWindAngle() {
        return windAngle;
    }

    /**
     * Set the water depth in the world model.
     * 
     * @param d
     *            Water depth in meters. Value is truncated into the range 0-
     *            {@value #MAX_WATER_DEPTH}.
     */
    public void setWaterDepth(float d) {
        if (d < 0) {
            d = 0;
        }
        if (d > MAX_WATER_DEPTH) {
            d = MAX_WATER_DEPTH;
        }
        waterDepth = d;
    }

    /**
     * Get the current water depth in the world model.
     * 
     * @return Current water depth in meters.
     */
    public float getWaterDepth() {
        return waterDepth;
    }

    /**
     * Return the current x-coordinate of the boat.
     * @return float containing boat's current x-coordinate.
     */
    public float getBoatX() {
        return boatX;
    }

    /**
     * Return the current y-coordinate of the boat.
     * @return float containing boat's current y-coordinate.
     */
    public float getBoatY() {
        return boatY;
    }

    public void step(float dt) {
        double ang = Math.toRadians(heading);
        boatX += Math.cos(ang) * boatSpeed * (10 * dt);
        boatY += Math.sin(ang) * boatSpeed * (10 * dt);
    }

}

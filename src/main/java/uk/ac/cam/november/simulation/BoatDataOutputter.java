package uk.ac.cam.november.simulation;

import java.io.IOException;

import uk.ac.cam.november.packet.Packet;

/**
 * This class controls the rate at which messages are output from the simulator.
 * 
 * @author Jamie Wood
 *
 */
public class BoatDataOutputter {

    public static final int COMPASS_INTERVAL = 1000;
    public static final int WIND_INTERVAL = 1000;
    public static final int DEPTH_INTERVAL = 1000;
    public static final int SPEED_INTERVAL = 1000;
    public static final int GPS_INTERVAL = 1000;

    private long lastCompassTime = System.currentTimeMillis();
    private long lastWindTime = System.currentTimeMillis();
    private long lastDepthTime = System.currentTimeMillis();
    private long lastSpeedTime = System.currentTimeMillis();
    private long lastGPSTime = System.currentTimeMillis();

    private Simulator simulator;

    public BoatDataOutputter(Simulator sim) {
        this.simulator = sim;
    }

    /**
     * Checks internal time-stamps, and outputs any packets which are due.
     */
    public void update() throws IOException {
        long nowTime = System.currentTimeMillis();
        if (lastCompassTime + COMPASS_INTERVAL < nowTime) {
            outputCompassPacket();
            lastCompassTime = nowTime;
        }
        if (lastWindTime + WIND_INTERVAL < nowTime) {
            outputWindPacket();
            lastWindTime = nowTime;
        }
        if (lastDepthTime + DEPTH_INTERVAL < nowTime) {
            outputDepthPacket();
            lastDepthTime = nowTime;
        }
        if (lastSpeedTime + SPEED_INTERVAL < nowTime) {
            outputSpeedPacket();
            lastSpeedTime = nowTime;
        }
        if (lastGPSTime + GPS_INTERVAL < nowTime) {
            outputGPSPacket();
            lastGPSTime = nowTime;
        }
    }

    /**
     * Create and output a vessel-heading packet, containing the current boat
     * heading from the world model.
     */
    private void outputCompassPacket() throws IOException {
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateVesselHeadingPacket(worldModel.getHeading());
        simulator.queueMessage(m);
    }

    /**
     * Create and output a wind-data packet, containing the wind speed and angle
     * from the world model.
     */
    private void outputWindPacket() throws IOException {
        WorldModel worldModel = simulator.getWorldModel();
        float angle = worldModel.getWindAngle() - worldModel.getHeading();
        if (angle < 0f)
            angle += 360.0f;
        if (angle > 360f)
            angle -= 360.0f;
        Packet m = DataGenerator.generateWindDataPacket(worldModel.getWindSpeed(), angle);
        simulator.queueMessage(m);
    }

    /**
     * Create and output a water-depth packet, containing the current water
     * depth from the world model.
     */
    private void outputDepthPacket() throws IOException {
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateWaterDepthPacket(worldModel.getWaterDepth(), 0f);
        simulator.queueMessage(m);
    }

    /**
     * Create and output a vessel-speed packet, containing the current boat
     * speed from the world model.
     */
    private void outputSpeedPacket() throws IOException {
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateSpeedPacket(worldModel.getBoatSpeed());
        simulator.queueMessage(m);
    }

    private static final float HALF_CIRCLE = 180.0f;
    private static final float FULL_CIRCLE = 360.f;
    /**
     * Create and output a gnss-position-data packet, containing a
     * representation of the current boat location from the world model.
     */
    private void outputGPSPacket() throws IOException {
        WorldModel worldModel = simulator.getWorldModel();
        float lat = (-worldModel.getBoatY() / WorldModel.depthMapHeight()) * HALF_CIRCLE;
        float lon = (worldModel.getBoatX() / WorldModel.depthMapWidth()) * FULL_CIRCLE;
        Packet m = DataGenerator.generateGPSPacket(lat, lon, 0);
        simulator.queueMessage(m);
    }

}

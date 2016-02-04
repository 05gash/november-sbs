package uk.ac.cam.november.simulation;

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

    private long lastCompassTime = System.currentTimeMillis();
    private long lastWindTime = System.currentTimeMillis();
    private long lastDepthTime = System.currentTimeMillis();
    private long lastSpeedTime = System.currentTimeMillis();

    private Simulator simulator;
    
    public BoatDataOutputter(Simulator sim){
        this.simulator = sim;
    }
    
    /**
     * Checks internal time-stamps, and outputs any packets which are due.
     */
    public void update() {
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
    }
    
    /**
     * Create and output a vessel-heading packet, containing the current boat heading from the world model.
     */
    private void outputCompassPacket(){
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateVesselHeadingPacket(worldModel.getHeading());
        simulator.queueMessage(m);
    }
    
    /**
     * Create and output a wind-data packet, containing the wind speed and angle from the world model.
     */
    private void outputWindPacket(){
        WorldModel worldModel = simulator.getWorldModel();
        float ang = worldModel.getWindAngle() - 180;
        Packet m = DataGenerator.generateWindDataPacket(worldModel.getWindSpeed(), worldModel.getWindAngle());
        simulator.queueMessage(m);
    }
    
    /**
     * Create and output a water-depth packet, containing the current water depth from the world model.
     */
    private void outputDepthPacket(){
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateWaterDepthPacket(worldModel.getWaterDepth(), 0f);
        simulator.queueMessage(m);
    }
    
    /**
     * Create and output a vessel-speed packet, containing the current boat speed from the world model.
     */
    private void outputSpeedPacket(){
        WorldModel worldModel = simulator.getWorldModel();
        Packet m = DataGenerator.generateSpeedPacket(worldModel.getBoatSpeed());
        simulator.queueMessage(m);
    }

}

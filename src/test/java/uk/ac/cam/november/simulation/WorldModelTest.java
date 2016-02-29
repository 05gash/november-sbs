package uk.ac.cam.november.simulation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WorldModelTest {

    @Test
    public void worldModelShouldDeaultToZeroValues() {
        WorldModel wm = new WorldModel(0, 0);
        assertEquals(wm.getBoatSpeed(), 0, 0.0001);
        assertEquals(wm.getHeading(), 0, 0.0001);
        assertEquals(wm.getWaterDepth(), 0, 0.0001);
        assertEquals(wm.getWindAngle(), 0, 0.0001);
        assertEquals(wm.getWindSpeed(), 0, 0.0001);
    }

    @Test
    public void worldModelShouldTruncateBoatSpeed() {
        WorldModel wm = new WorldModel(0, 0);
        wm.setBoatSpeed(-10);
        assertEquals(0, wm.getBoatSpeed(), 0.001);
        wm.setBoatSpeed(WorldModel.MAX_BOAT_SPEED + 100);
        assertEquals(WorldModel.MAX_BOAT_SPEED, wm.getBoatSpeed(), 0.001);
    }

    @Test
    public void worldModelShouldWrapHeading() {
        WorldModel wm = new WorldModel(0, 0);
        wm.setHeading(-10);
        assertEquals(350, wm.getHeading(), 0.001);
        wm.setHeading(400);
        assertEquals(40, wm.getHeading(), 0.001);
    }

    @Test
    public void worldModelShouldTruncateWaterDepth() {
        WorldModel wm = new WorldModel(0, 0);
        wm.setWaterDepth(-10);
        assertEquals(0, wm.getWaterDepth(), 0.001);
        wm.setWaterDepth(WorldModel.MAX_WATER_DEPTH + 100);
        assertEquals(WorldModel.MAX_WATER_DEPTH, wm.getWaterDepth(), 0.001);
    }

    @Test
    public void worldModelShouldWrapWindAngle() {
        WorldModel wm = new WorldModel(0, 0);
        wm.setWindAngle(-10);
        assertEquals(350, wm.getWindAngle(), 0.001);
        wm.setWindAngle(400);
        assertEquals(40, wm.getWindAngle(), 0.001);
    }

    @Test
    public void worldModelShouldTruncateWindSpeed() {
        WorldModel wm = new WorldModel(0, 0);
        wm.setWindSpeed(-10);
        assertEquals(0, wm.getWindSpeed(), 0.001);
        wm.setWindSpeed(WorldModel.MAX_WIND_SPEED + 100);
        assertEquals(WorldModel.MAX_WIND_SPEED, wm.getWindSpeed(), 0.001);
    }

}

package uk.ac.cam.november.simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import uk.ac.cam.november.packet.Fields;
import uk.ac.cam.november.packet.Packet;

public class DataGeneratorTest {
    
    /* createDefaultPacket */
    
    @Test
    public void defaultPacketShouldHaveCorrectDefaultValues(){
        Packet packet = DataGenerator.createDefaultPacket();
        assertEquals(DataGenerator.DEFAULT_PRIORITY, packet.getPrio());
        assertEquals(DataGenerator.DEFAULT_SRC, packet.getSrc());
        assertEquals(DataGenerator.DEFAULT_DEST, packet.getDst());
    }
    
    /* generateVesselHeadingPacket */
    
    @Test
    public void vesselHeadingPacketsShouldCheckInputRange() {
        try{ DataGenerator.generateVesselHeadingPacket(-1f, 0f, 0f); fail("Heading did not catch <0"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateVesselHeadingPacket(370f, 0f, 0f); fail("Heading did not catch >360"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateVesselHeadingPacket(0f, -200f, 0f); fail("Deviation did not catch <-180"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateVesselHeadingPacket(0f, 200f, 0f); fail("Deviation did not catch >180"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateVesselHeadingPacket(0f, 0f, -200f); fail("Variation did not catch <-180"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateVesselHeadingPacket(0f, 0f, 200f); fail("Variation did not catch >180"); } catch(IllegalArgumentException e) {}
    }
    
    @Test
    public void vesselHeadingPacketShouldContainCorrectFields() {
        Packet packet = DataGenerator.generateVesselHeadingPacket(100f, 1f, -1f);
        Fields fields = packet.getFields();
        
        assertEquals(packet.getPgn(), 127250);
        
        assertEquals(fields.getHeading(), 100.0, 0.01);
        assertEquals(fields.getDeviation(), 1.0, 0.01);
        assertEquals(fields.getVariation(), -1.0, 0.01);
    }
    
    /* generateWaterDepthPacket */
    
    @Test
    public void waterDepthPacketShouldCheckInputRange() {
        try{ DataGenerator.generateWaterDepthPacket(-1f, 0); fail("Water Depth did not catch <0"); } catch(IllegalArgumentException e) {}
    }
    
    @Test
    public void waterDepthPacketShouldContainCorrectFields() {
        Packet packet = DataGenerator.generateWaterDepthPacket(100f, 1f);
        Fields fields = packet.getFields();
        
        assertEquals(packet.getPgn(), 128267);
        
        assertEquals(fields.getDepth(), 100.0, 0.01);
        assertEquals(fields.getOffset(), 1.0, 0.01);
    }
    
    /* generateWindDataPacket */
    
    @Test
    public void windDepthPacketShouldCheckInputRange() {
        try{ DataGenerator.generateWindDataPacket(-1f, 0f); fail("Wind Speed did not catch <0"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateWindDataPacket(1f, -10f); fail("Wind Angle did not catch <0"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateWindDataPacket(1f, 370f); fail("Wind Angle did not catch >360"); } catch(IllegalArgumentException e) {}
    }
    
    @Test
    public void windDataPacketShouldContainCorrectFields() {
        Packet packet = DataGenerator.generateWindDataPacket(100f, 1f);
        Fields fields = packet.getFields();
        
        assertEquals(packet.getPgn(), 130306);
        
        assertEquals(fields.getWindSpeed(), 100.0, 0.01);
        assertEquals(fields.getWindAngle(), 1.0, 0.01);
        assertEquals(fields.getReference(), "Apparent");
    }
    
    /* generateSpeedPacket */
    
    @Test
    public void speedPacketShouldCheckInputRange() {
        try{ DataGenerator.generateSpeedPacket(-1f); fail("Vessel speed value did not catch <0"); } catch(IllegalArgumentException e) {}
    }
    
    @Test
    public void speedPacketShouldContainCorrectFields(){
        Packet packet = DataGenerator.generateSpeedPacket(100f);
        Fields fields = packet.getFields();
        
        assertEquals(packet.getPgn(), 128259);
        
        assertEquals(fields.getSpeedWaterReferenced(), 100.0, 0.01);
        assertEquals(fields.getSpeedWaterReferencedType(), "Paddle wheel");
    }
    
    /* generateGPSPacket */
    
    @Test
    public void gpsPacketShouldCheckInputRange(){
        try{ DataGenerator.generateGPSPacket(-91f, 0, 0); fail("Latitude value did not catch <-90"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateGPSPacket(91f, 0, 0); fail("Latitude value did not catch >90"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateGPSPacket(0, -181f, 0); fail("Longitude value did not catch <-180"); } catch(IllegalArgumentException e) {}
        try{ DataGenerator.generateGPSPacket(0, 181f, 0); fail("Longitude value did not catch >180"); } catch(IllegalArgumentException e) {}
    }
    
    @Test
    public void gpsPacketShouldContainCorrectFields(){
        Packet packet = DataGenerator.generateGPSPacket(-45.54367f, 37.12345f, 123.47f);
        Fields fields = packet.getFields();
        
        assertEquals(packet.getPgn(), 129029);
        
        assertEquals(fields.getLatitude(), -45.54367f, 0.0000001);
        assertEquals(fields.getLongtitude(), 37.12345f, 0.0000001);
        assertEquals(fields.getAltitude(), 123.47f, 0.0000001);
    }
    
}

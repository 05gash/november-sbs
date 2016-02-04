package uk.ac.cam.november.simulation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import uk.ac.cam.november.packet.BoatHeadingFields;
import uk.ac.cam.november.packet.BoatSpeedFields;
import uk.ac.cam.november.packet.Packet;
import uk.ac.cam.november.packet.WaterDepthFields;
import uk.ac.cam.november.packet.WindDataFields;

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
        BoatHeadingFields fields = (BoatHeadingFields)packet.getFields();
        
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
        WaterDepthFields fields = (WaterDepthFields) packet.getFields();
        
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
        WindDataFields fields = (WindDataFields) packet.getFields();
        
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
        BoatSpeedFields fields = (BoatSpeedFields) packet.getFields();
        
        assertEquals(packet.getPgn(), 128259);
        
        assertEquals(fields.getSpeedWaterReferenced(), 100.0, 0.01);
        assertEquals(fields.getSpeedWaterReferencedType(), "Paddle wheel");
    }
    
}

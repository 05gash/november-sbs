package uk.ac.cam.november.simulation;

import java.util.Date;

import uk.ac.cam.november.message.BoatHeadingFields;
import uk.ac.cam.november.message.BoatSpeedFields;
import uk.ac.cam.november.message.Message;
import uk.ac.cam.november.message.WaterDepthFields;
import uk.ac.cam.november.message.WindDataFields;

/**
 * This class creates {@code Message} objects which emulate those produced by
 * the CANBoat input stage of the project. This is useful for testing the
 * data-parsing module, and for demonstration purposes.
 * 
 * @author Jamie Wood
 *
 */
public class DataGenerator {

    /** Priority for generated messages. 0 is highest priority, 7 is lowest. */
    public static int DEFAULT_PRIORITY = 2;
    /** Arbitrary source address for the generated packets. */
    public static int DEFAULT_SRC = 36;
    /**
     * Destination of 255 means broadcast to all devices on NMEA 2000 network.
     */
    public static int DEFAULT_DEST = 255;

    /**
     * Creates a blank packet containing the essential fields such as timestamp,
     * priority, source and destination. Default values are:
     * <ul>
     * <li>timestamp: current time and date</li>
     * <li>priority: 2</li>
     * <li>source: 36</li>
     * <li>destination: 255 (broadcast)</li>
     * </ol>
     * Also creates the {@code fields} array ready to be populated with data.
     * 
     * @return {@code Message} containing the packet
     */
    public static Message createDefaultPacket() {
        Message packet = new Message();
        packet.setTimestamp(new Date());
        packet.setPrio(DEFAULT_PRIORITY);
        packet.setSrc(DEFAULT_SRC);
        packet.setDst(DEFAULT_DEST);

        return packet;
    }

    /**
     * Creates and returns a vesselHeading packet with the specified heading, 0
     * degrees deviation, 0 degrees variation.
     * 
     * @param heading
     *            compass heading in degrees from North
     * @return A {@code Message} containing the packet
     * @see {@link #generateVesselHeadingPacket(double, double, double)}
     */
    public static Message generateVesselHeadingPacket(float heading) {
        return generateVesselHeadingPacket(heading, 0.0f, 0.0f);
    }

    /**
     * Creates and returns a vesselHeading packet with the specified heading,
     * deviation and variation. See
     * <a href="http://www.cockpitcards.co.uk/page15.htm">this</a> webpage for
     * definitions.
     * 
     * @param heading
     *            Compass heading from North (degrees)
     * @param deviation
     *            Magnetic deviation from true value caused by ferrous metal on
     *            vessel (degrees)
     * @param variation
     *            Difference from Magnetic North to True North (degrees)
     * @return A {@code Message} containing the packet
     */
    public static Message generateVesselHeadingPacket(float heading, float deviation, float variation) {
        if (heading < 0.0 || heading > 360.0) {
            throw new IllegalArgumentException("Heading value must be between 0 and 360 degrees");
        }
        if (deviation < -180.0 || deviation > 180.0) {
            throw new IllegalArgumentException("Deviation value must be between -180 and 180 degrees");
        }
        if (variation < -180.0 || variation > 180.0) {
            throw new IllegalArgumentException("Variation value must be between -180 and 180 degrees");
        }

        Message packet = createDefaultPacket();

        packet.setPgn(127250);
        packet.setDescription("Vessel Heading");

        BoatHeadingFields fields = new BoatHeadingFields();

        fields.setSID(0);
        fields.setHeading(heading);
        fields.setDeviation(deviation);
        fields.setVariation(variation);
        fields.setReference("Magnetic");

        packet.setFields(fields);

        return packet;
    }

    /**
     * Creates and returns a waterDepth packet with the specified water depth
     * and zero offset.
     * 
     * @param waterDepth
     *            Depth of water below the sensor (meters)
     * @param offset
     *            Distance from sensor to surface (positive) or keel (negative)
     * @return A {@code Message} containing the packet
     */
    public static Message generateWaterDepthPacket(float waterDepth, float offset) {
        if (waterDepth < 0.0) {
            throw new IllegalArgumentException("Water depth value must be >= 0 meters");
        }

        Message packet = createDefaultPacket();

        packet.setPgn(128267);
        packet.setDescription("Water Depth");

        WaterDepthFields fields = new WaterDepthFields();

        fields.setSID(0);
        fields.setDepth(waterDepth);
        fields.setOffset(offset);

        packet.setFields(fields);

        return packet;
    }

    /**
     * Creates and returns a windData packet with the specified wind speed and
     * wind angle.
     * 
     * @param windSpeed
     *            Speed of wind (meters per second)
     * @param windAngle
     *            Angle of wind (degrees, clockwise from bow)
     * @return A {@code Message} containing the packet
     */
    public static Message generateWindDataPacket(float windSpeed, float windAngle) {
        if (windSpeed < 0.0) {
            throw new IllegalArgumentException("Wind speed value must be >= 0 m/s");
        }
        if (windAngle < 0.0 || windAngle > 360.0) {
            throw new IllegalArgumentException("Wind angle value must be between 0 and 360 degrees");
        }

        Message packet = createDefaultPacket();

        packet.setPgn(130306);
        packet.setDescription("Wind Data");

        WindDataFields fields = new WindDataFields();

        fields.setSID(0);
        fields.setWindSpeed(windSpeed);
        fields.setWindAngle(windAngle);
        fields.setReference("Apparent");

        packet.setFields(fields);

        return packet;
    }

    /**
     * Creates and returns a speed packet with the specified speed <b>relative
     * to water</b>.
     * 
     * @param vesselSpeed
     *            Speed of vessel relative to water (meters per second)
     * @return A {@code Message} containing the packet
     */
    public static Message generateSpeedPacket(float vesselSpeed) {
        if (vesselSpeed < 0.0) {
            throw new IllegalArgumentException("Vessel speed value must be >= 0 m/s");
        }

        Message packet = createDefaultPacket();

        packet.setPgn(128259);
        packet.setDescription("Speed");

        BoatSpeedFields fields = new BoatSpeedFields();

        fields.setSID(0);
        fields.setSpeedWaterReferenced(vesselSpeed);
        fields.setSpeedWaterReferencedType("Paddle wheel");

        packet.setFields(fields);

        return packet;
    }

}

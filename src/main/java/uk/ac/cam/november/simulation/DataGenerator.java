package uk.ac.cam.november.simulation;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonObject;

public class DataGenerator {

	/*
	 * Example JSON output (from CAN Boat using sample data on Wiki)
	 * 
	 * {"timestamp":"2011-11-24-22:42:04.388","prio":2,"src":36,"dst":255,"pgn":127251,"description":"Rate of Turn","fields":{"SID":125,"Rate":0.029200}}
	 * {"timestamp":"2011-11-24-22:42:04.390","prio":2,"src":36,"dst":255,"pgn":127250,"description":"Vessel Heading","fields":{"SID":0,"Heading":182.4,"Deviation":0.0,"Variation":0.0,"Reference":"Magnetic"}}
	 * {"timestamp":"2011-11-24-22:42:04.437","prio":2,"src":36,"dst":255,"pgn":130306,"description":"Wind Data","fields":{"SID":177,"Wind Speed":0.92,"Wind Angle":353.4,"Reference":"Apparent"}}
	 * 
	 */
	
	/** Priority for generated messages. 0 is highest priority, 7 is lowest. */
	public static int DEFAULT_PRIORITY = 2;
	/** Arbitrary source address for the generated packets. */
	public static int DEFAULT_SRC = 36;
	/** Destination of 255 means broadcast to all devices on NMEA 2000 network. */
	public static int DEFAULT_DEST = 255;
	
	/**
	 * Creates a blank packet containing the essential fields such as timestamp, priority, source and destination.
	 * Default values are:<ul>
	 * <li>timestamp: current time and date</li>
	 * <li>priority: 2</li>
	 * <li>source: 36</li>
	 * <li>destination: 255 (broadcast)</li>
	 * </ol>
	 * Also creates the {@code fields} array ready to be populated with data.
	 * @return  {@code JsonObject} containing the packet
	 */
	public static JsonObject createDefaultPacket() {
		JsonObject packet = new JsonObject();
		
		SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS");
		
		packet.addProperty("timestamp", timestampFormat.format(new Date()));
		packet.addProperty("prio", DEFAULT_PRIORITY);
		packet.addProperty("src", DEFAULT_SRC);
		packet.addProperty("dst", DEFAULT_DEST);
		
		packet.add("fields", new JsonObject());
		
		return packet;
	}
	
	/**
	 * Creates and returns a vesselHeading packet with the specified heading, 0 degrees deviation, 0 degrees variation.
	 * @param heading  compass heading in degrees from North
	 * @return         A {@code JsonObject} containing the packet
	 * @see            {@link #generateVesselHeadingPacket(double, double, double)}
	 */
	public static JsonObject generateVesselHeadingPacket(double heading) {
		return generateVesselHeadingPacket(heading, 0.0, 0.0);
	}
	
	/**
	 * Creates and returns a vesselHeading packet with the specified heading, deviation and variation.
	 * See <a href="http://www.cockpitcards.co.uk/page15.htm">this</a> webpage for definitions.
	 * @param heading    Compass heading from North (degrees)	
	 * @param deviation  Magnetic deviation from true value caused by ferrous metal on vessel (degrees)
	 * @param variation  Difference from Magnetic North to True North (degrees)
	 * @return           A {@code JsonObject} containing the packet
	 */
	public static JsonObject generateVesselHeadingPacket(double heading, double deviation, double variation) {
		if ( heading < 0.0 || heading > 360.0) {
			throw new IllegalArgumentException("Heading value must be between 0 and 360 degrees");
		}
		if ( deviation < -180.0 || deviation > 180.0) {
			throw new IllegalArgumentException("Deviation value must be between -180 and 180 degrees");
		}
		if ( variation < -180.0 || variation > 180.0) {
			throw new IllegalArgumentException("Variation value must be between -180 and 180 degrees");
		}
		
		JsonObject packet = createDefaultPacket();
		
		packet.addProperty("pgn", 127250);
		packet.addProperty("description", "Vessel Heading");
		
		JsonObject fields = packet.getAsJsonObject("fields");
		
		fields.addProperty("SID", 0);
		fields.addProperty("Heading", heading);
		fields.addProperty("Deviation", deviation);
		fields.addProperty("Variation", variation);
		fields.addProperty("Reference", "Magnetic");
		
		return packet;
	}
	
	/**
	 * Creates and returns a waterDepth packet with the specified water depth and zero offset.
	 * @param waterDepth  Depth of water below the sensor (meters)
	 * @param offset      Distance from sensor to surface (positive) or keel (negative)
	 * @return            A {@code JsonObject} containing the packet
	 */
	public static JsonObject generateWaterDepthPacket(double waterDepth, double offset) {
		if ( waterDepth < 0.0 ) {
			throw new IllegalArgumentException("Water depth value must be >= 0 meters");
		}
		
		JsonObject packet = createDefaultPacket();
		
		packet.addProperty("pgn", 128267);
		packet.addProperty("description", "Water Depth");
		
		JsonObject fields = packet.getAsJsonObject("fields");
		
		fields.addProperty("SID", 0);
		fields.addProperty("Depth", waterDepth);
		fields.addProperty("Offset", offset);
		
		return packet;
	}
	
}

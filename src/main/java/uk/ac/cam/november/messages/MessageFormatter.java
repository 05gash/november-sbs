package uk.ac.cam.november.messages;

import java.util.LinkedList;

import uk.ac.cam.november.boot.ShutDown;
import uk.ac.cam.november.buttons.ButtonNames;
import uk.ac.cam.november.decoder.AlertMessage;
import uk.ac.cam.november.decoder.BoatState;
import uk.ac.cam.november.decoder.MessageDecoder;
import uk.ac.cam.november.location.LatLng;
import uk.ac.cam.november.location.LocationUtil;
import uk.ac.cam.november.location.Port;
import uk.ac.cam.november.packet.Packet;

/**
 * The purpose of this class is to respond to determine which button was pressed,
 * poll the StateDecoder for data information
 * format a message corresponding to the button pressed,
 * and assign a priority to the message before sending it to the MessageHandler class.
 * 
 *
 * @author Alan Tang
 *
 */
public class MessageFormatter {

	private static final int MESSAGE_PRIORITY = 1;
	private static final int ALERT_PRIORITY = 2;
	private static final int SHUT_DOWN_PRIORITY = 3;

	private static MessageDecoder mDecoder = null;

	/**
	 * Sets the MessageDecoder from which this class will receive its values. 
	 */
	public static void setDecoder(MessageDecoder decoder)
	{
		mDecoder = decoder;
	}

	// Prevents instantiation
	private MessageFormatter() {}

	/**
	 * Gets data which corresponds with input button, formats it, and sends it to the Messagehandler
	 * 
	 * @param buttonName The name of the button that was pressed.
	 */
	public static void handleButtonPress(String buttonName)
	{
	    /* If the shut down button has been pressed, the system will announce
	     * a shutdown message, and then it will turn off */
		if (buttonName.compareTo(ButtonNames.SHUT_DOWN) == 0) {
			Message shutDownMessage = new Message("Turning the system completely off", SHUT_DOWN_PRIORITY);
			MessageHandler.receiveMessage(shutDownMessage);

			/** Executing on actual shutdown operation */
			ShutDown.shutDown();
			return;
		}

		String formattedString = formatButtonPress(buttonName); 

		/* Assign priority and wrap in Message Object */
		Message m = new Message(formattedString, MESSAGE_PRIORITY);
		
		/* Calls MessageHandler */
		System.out.println("Sending Message: '" + formattedString +"'");
		MessageHandler.receiveMessage(m);
	}

	private static String formatButtonPress(String buttonName)
	{
		String formattedString;

		switch (buttonName)
		{
		
		/* BoatSpeed */
		case ButtonNames.BOAT_SPEED:
			formattedString = formatBoatSpeedButton();
			break;
			
		/* Heading */
		case ButtonNames.COMPASS_HEADING:
			formattedString = formatCompassHeadingButton();
			break;
			
		/* NearestPort */
		case ButtonNames.NEAREST_PORT:
			formattedString = formatNearestPortButton();
			break;
			
		/* WaterDepth */
		case ButtonNames.WATER_DEPTH:
			formattedString = formatWaterDepthButton();
			break;
			
		/* WindDirection */
		case ButtonNames.WIND_DIRECTION:
			formattedString = formatWindDirButton();
			break;
			
		/* WindSpeed */
		case ButtonNames.WIND_SPEED:
			formattedString = formatWindSpeedButton();
			break;

		default: 
			// Should not reach here
			System.err.println("Invalid button name: " + buttonName);
			throw new IllegalArgumentException("Invalid button name: " + buttonName);

		}

		return formattedString;
	}

	private static String formatNearestPortButton() {
		BoatState state = mDecoder.getState();
		LatLng myLoc = new LatLng(state.getLatitude(), state.getLongtitude());
		Port p = LocationUtil.nearestPort(myLoc);
		double dist = LocationUtil.distance(myLoc, p.location);
		double bearing = LocationUtil.initialBearing(myLoc, p.location);
		String distString = formatDistance(dist);
		String bearingString = truncateFloat((float)bearing);
		return distString + " at " + bearingString + " degrees to " + p.name;
	}

	private static String formatWindSpeedButton() {
		float speed = mDecoder.getState().getWindSpeed();
		String strSpd = truncateFloat(speed);

		return strSpd + " meters per second";
	}

	private static String formatWindDirButton() {
		float dir = mDecoder.getState().getWindAngle();
		String strDir = String.format("%.0f", dir);

		return strDir + " degrees from head";
	}

	private static String formatWaterDepthButton() {
		float depth = mDecoder.getState().getDepth();
		String strDpth = truncateFloat(depth);

		return strDpth + " meters deep";
	}

	private static String formatBoatSpeedButton() {

		float speed = mDecoder.getState().getSpeedWaterReferenced();
		String strSpd = truncateFloat(speed);

		return strSpd + " meters per second";
	}

	private static String formatCompassHeadingButton() {
		float heading = mDecoder.getState().getHeading();
		String strHdg = String.format("%.0f", heading);

		return strHdg + " degrees from north";
	}

	private static String formatDistance(double distance){
		String unit = "m";
		if(distance > 1000){
			distance /= 1000;
			unit = "km";
		}
		return truncateFloat((float)distance) + unit;
	}

	private static String truncateFloat(float v)
	{
		int l = 1; 

		if( v > 10 || (Math.abs (v - Math.round(v) )) < 0.1 )
		{
			l = 0; 
		}
		String data = String.format("%."+ l  +"f", v);
		return data;
	}

	public static void handleAlert(AlertMessage alert)
	{
		int type = alert.getAlertType();
		int sensor = alert.getSensor();

		String formattedString = formatAlert(alert);

		// assign priority and wrap in Message Object
		Message m = new Message(formattedString, ALERT_PRIORITY);

		// call MessageHandler
		System.out.println("Sending Alert Message: '" + formattedString +"'");
		MessageHandler.receiveMessage(m);
	}

	private static String formatAlert(AlertMessage alert)
	{
		int type = alert.getAlertType();
		int sensor = alert.getSensor();

		String formattedString = "Warning: ";

		switch (sensor)
		{
		
		/* WaterDepth */
		case 0:
			formattedString += formatDepthAlert(alert);
			break;
		
		/* WindSpeed */
		case 1:
			formattedString += formatWindSpeedAlert(alert);
			break;
			
		/* WindDirection */
		case 2:
			formattedString += formatWindDirectionAlert(alert);
			break;
			
		/* Heading */
		case 3:
			formattedString += formatHeadingAlert(alert);
			break;
			
		/* BoatSpeed */
		case 4:
			formattedString += formatBoatSpeedAlert(alert);
			break;

		default:
			// Should not reach here:
				System.err.println("Invalid alert sensor type: " + sensor);
		throw new IllegalArgumentException("Invalid alert sensor type: " + sensor);
		}

		return formattedString;
	}



	private static String formatDepthAlert(AlertMessage alert) {
		String formattedString = "";
		switch (alert.getAlertType())
		{
		
		/* CriticalChange */
		case 0:
			formattedString += "rapid change in water depth";
			break;
		
		/* CriticalMax (not implemented in Decoder) */
		case 1:
			formattedString += "entering deep water?";
			break;
		
		/* CriticalMin */
		case 2:
			formattedString += "entering shallow water";
			break;
			
		/* TimeOut */
		case 3:
			formattedString += "wate depth sensor is unresponsive";
			break;

		default:
			// Should not reach here:
			System.err.println("Invalid alert type: " + alert.getAlertType());
			throw new IllegalArgumentException("Invalid alert type: " + alert.getAlertType());
		}
		return formattedString;
	}

	private static String formatWindSpeedAlert(AlertMessage alert) {
		String formattedString = "";
		switch (alert.getAlertType())
		{
		
		/* CriticalChange */
		case 0:
			formattedString += "rapid change in wind speed";
			break;
		
		/* CriticalMax */
		case 1:
			formattedString += "high winds";
			break;
			
		/* CriticalMin (not implemented in Decoder) */
		case 2:
			formattedString += "wind speed is low?";
			break;
			
		/* TimeOut */
		case 3:
			formattedString += "wind speed sensor is unresponsive";
			break;

		default:
			// Should not reach here:
			System.err.println("Invalid alert type: " + alert.getAlertType());
			throw new IllegalArgumentException("Invalid alert type: " + alert.getAlertType());
		}
		return formattedString;
	}

	private static String formatWindDirectionAlert(AlertMessage alert) {
		String formattedString = "";
		switch (alert.getAlertType())
		{
		/* CriticalChange */
		case 0:
			formattedString += "rapid change in wind direction";
			break;
		
		/* CriticalMax (not implemented in Decoder) */
		case 1:
			formattedString += "wind direction is high?";
			break;
		
		/* CriticalMin (not implemented in Decoder) */
		case 2:
			formattedString += "wind direction is low?";
			break;
			
		/* TimeOut */
		case 3:
			formattedString += "wind direction sensor is unresponsive";
			break;

		default:
			// Should not reach here:
			System.err.println("Invalid alert type: " + alert.getAlertType());
			throw new IllegalArgumentException("Invalid alert type: " + alert.getAlertType());
		}
		return formattedString;
	}


	private static String formatHeadingAlert(AlertMessage alert) {
		String formattedString = "";
		switch (alert.getAlertType())
		{
		
		/* CriticalChange */
		case 0:
			formattedString += "rapid change in boat heading";
			break;
		
		/* CriticalMax (not implemented in Decoder) */
		case 1:
			formattedString += "boat heading is high?";
			break;
		
		/* CriticalMin (not implemented in Decoder) */
		case 2:
			formattedString += "boat heading is low?";
			break;
		
		/* TimeOut */
		case 3:
			formattedString += "boat heading sensor is unresponsive";
			break;

		default:
			// Should not reach here:
			System.err.println("Invalid alert type: " + alert.getAlertType());
			throw new IllegalArgumentException("Invalid alert type: " + alert.getAlertType());
		}
		return formattedString;
	}

	private static String formatBoatSpeedAlert(AlertMessage alert) {
		String formattedString = "";
		switch (alert.getAlertType())
		{
	
		/* CriticalChange */
		case 0:
			formattedString += "rapid change in boat speed";
			break;
		
		/* CriticalMax */
		case 1:
			formattedString += "traveling very fast";
			break;
		
		/* CriticalMin (not implemented in Decoder) */
		case 2:
			formattedString += "traveling very slow";
			break;
			
		/* TimeOut */
		case 3:
			formattedString += "boat speed sensor is unresponsive";
			break;

		default:
			// Should not reach here:
			System.err.println("Invalid alert type: " + alert.getAlertType());
			throw new IllegalArgumentException("Invalid alert type: " + alert.getAlertType());
		}
		return formattedString;
	}

}

package uk.ac.cam.november.messages;

import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.cam.november.boot.ShutDown;
import uk.ac.cam.november.buttons.ButtonNames;
import uk.ac.cam.november.decoder.BoatState;
import uk.ac.cam.november.decoder.MessageDecoder;

/**
 * 
 * @author Alan Tang
 * 
 * The purpose of this class is to respond to determine which button was pressed,
 * poll the StateDecoder for data information
 * format a message corresponding to the button pressed,
 * and assign a priority to the message before sending it to the MessageHandler class
 *
 */
public class MessageFormatter {
    
    private static final int MESSAGE_PRIORITY = 1;
    private static final int ALERT_PRIORITY = 2;
    private static final int SHUT_DOWN_PRIORITY = 3;

    private static Logger logger = Logger.getLogger("uk.ac.cam.november.messages.MessageFormatter");
    
    private static MessageDecoder mDecoder = null;
    
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
    public static void handleButtonPress(ButtonNames buttonName)
    {
	// If the shut down button has been pressed,
	// we will deal with it separately by turning the system off
	if (buttonName == ButtonNames.SHUT_DOWN) {
		// Before shutting down, we will announce a shut down message loudly.
		Message shutDownMessage = new Message("Turning the system completely off", SHUT_DOWN_PRIORITY);
        MessageHandler.receiveMessage(shutDownMessage);
		
		// Excecuting an actual shut down operation;
		ShutDown.shutDown();
		return;
	}
        
        //poll StateDecoder
        float sensorData = pollStateDecoder(buttonName);
        
        // format the sensor data into a string;
        String formattedString = formatMessage(sensorData, buttonName);
        
        // assign priority and wrap in Message Object
        Message m = new Message(formattedString, MESSAGE_PRIORITY);
        
        // call MessageHandler
        MessageHandler.receiveMessage(m);
    }
    
 /*   public static void handleAlert(AlertMessage alert)
    {
        
        logger.log(Level.WARNING, "handlerAlert not implemented yet");
        // TODO: poll StateDecoder
        
        // TODO: format the sensor data into a string;
        
        // assign priority and wrap in Message Object
        // TODO: Come up with a sensible priority scheme.
        Message m = new Message("Emergency Bartok", ALERT_PRIORITY);
        
        // call MessageHandler
        MessageHandler.receiveMessage(m);
        
        
    }
*/

    
    private static float pollStateDecoder(ButtonNames buttonName)
    {
        if(mDecoder == null)
        {
            logger.log(Level.SEVERE,"MessageDecoder not set");
            throw new NullPointerException();
        }
        
        BoatState state = mDecoder.getState();
        switch(buttonName)
        {
        case BOAT_SPEED:
            return state.getSpeedWaterReferenced();
        case COMPASS_HEADING:
            return state.getHeading();
	case NEAREST_PORT:
            // TODO(ml693): decide of what is going to happen on button click which asks for nearest port.
            // So far only latitude is returned, but there are also longitude and altitude values!
            return state.getLatitude();
        case WATER_DEPTH:
            return state.getDepth();
        case WIND_DIRECTION:
            return state.getWindAngle();
        case WIND_SPEED:
            return state.getWindSpeed();
            
        default:
            // Should not reach here
            logger.log(Level.SEVERE, "Invalid button name: " + buttonName);
            System.err.println("Invalid button name: " + buttonName);
            throw new IllegalArgumentException("Invalid button name: " + buttonName);
        }
    }
    
    /*
     * Given a data value and the field that it corresponds with, this creates a formatted string
     * that can be read by the MessageHandler
     * 
     */
    private static String formatMessage(float dataValue, ButtonNames buttonName)
    {
        //Only reads a decimal point if less than 10
        int l = dataValue > 10 ? 0 : 1;
        String data = String.format("%."+ l  +"f",dataValue);
        
        switch(buttonName)
        {
        case NEAREST_PORT:
            // TODO(ml693): decide how to handle button asking for nearest port;
            return data + " latitude";
        case WATER_DEPTH:
            return data + " meters deep";
        case WIND_SPEED:
            return data + " meters per second";
        case WIND_DIRECTION:
            return data + " degrees from head";
        case COMPASS_HEADING:
            return data + " degrees from north";
        case BOAT_SPEED:
            return data + " meters per second";
        
        default:
            // Should not reach here
            logger.log(Level.SEVERE, "Formatting error: " + data + " " + buttonName);
            System.err.println("Formatting error: " + data + " " + buttonName);
            throw new IllegalArgumentException("Formatting error: " + data + " " + buttonName);
        }
        
    }

    
}

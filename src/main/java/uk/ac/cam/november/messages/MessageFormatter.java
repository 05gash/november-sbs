package uk.ac.cam.november.messages;

import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.cam.november.alerts.AlertMessage;
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
    public static void handleButtonPress(String buttonName)
    {
        
        //poll StateDecoder
        float sensorData = pollStateDecoder(buttonName);
        
        // format the sensor data into a string;
        String formattedString = formatMessage(sensorData, buttonName);
        
        // assign priority and wrap in Message Object
        //TODO: Come up with a sensible priority scheme.
        Message m = new Message(formattedString, MESSAGE_PRIORITY);
        
        // call MessageHandler
        MessageHandler.receiveMessage(m);
    }

    
    public static void handleAlert(AlertMessage alert)
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

    
    private static float pollStateDecoder(String buttonName)
    {
        if(mDecoder == null)
        {
            logger.log(Level.SEVERE,"MessageDecoder not set");
            throw new NullPointerException();
        }
        
        BoatState state = mDecoder.getState();
        switch(buttonName)
        {
        case ButtonNames.BOAT_SPEED:
            return state.getSpeedWaterReferenced();
        case ButtonNames.COMPASS_HEADING:
            return state.getHeading();
        case ButtonNames.WATER_DEPTH:
            return state.getDepth();
        case ButtonNames.WIND_DIRECTION:
            return state.getWindAngle();
        case ButtonNames.WIND_SPEED:
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
    private static String formatMessage(float dataValue, String buttonName)
    {
        String data = String.format("%.2f", dataValue);
        
        switch(buttonName)
        {
        case ButtonNames.WATER_DEPTH:
            return data + " meters deep";
        case ButtonNames.WIND_SPEED:
            return data + " meters per second";
        case ButtonNames.WIND_DIRECTION:
            return data + " degrees from head";
        case ButtonNames.COMPASS_HEADING:
            return data + " degrees from north";
        case ButtonNames.BOAT_SPEED:
            return data + " meters per second";
        
        default:
            // Should not reach here
            logger.log(Level.SEVERE, "Formatting error: " + data + " " + buttonName);
            System.err.println("Formatting error: " + data + " " + buttonName);
            throw new IllegalArgumentException("Formatting error: " + data + " " + buttonName);
        }
        
    }
    
    
    
}

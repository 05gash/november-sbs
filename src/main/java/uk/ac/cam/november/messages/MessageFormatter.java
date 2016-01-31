package uk.ac.cam.november.messages;

import uk.ac.cam.november.StateDecoder;
import uk.ac.cam.november.buttons.ButtonNames;

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
    
    private static StateDecoder mDecoder;
    
    private MessageFormatter()
    {
        mDecoder = new StateDecoder();
    }
    
    /*
     * Returns a reference to the MessageFormatter Singleton, and initializes one if one does not exist.
     * @return a reference to the MessageFormatter Singleton
     *
    public static MessageFormatter getInstance()
    {
        if(formatterSingleton == null)
        {
            formatterSingleton = new MessageFormatter();
        }
        return formatterSingleton;
    }
    */
    
    /**
     * 
     * @param buttonName The name of the button that was pressed.
     */
    public void handleButtonPress(String buttonName)
    {
        
        //poll StateDecoder
        String sensorData = pollStateDecoder(buttonName);
        
        // format the sensor data into a string;
        String formattedString = formatMessage(sensorData, buttonName);
        
        // assign priority and wrap in Message Object
        Message m = new Message(formattedString, 1);
        
        // call MessageHandler
        MessageHandler.receiveMessage(m);
    }
  
    
    //TODO: determine format of polling from the state decoder.
    private String pollStateDecoder(String buttonName)
    {
        return this.mDecoder.getRecent(buttonName);
    }
    
    /*
     * Given a data value and the field that it corresponds with, this creates a formatted string
     * that can be read by the MessageHandler
     * 
     * @param data the value of the data
     * @param buttonName the data field
     * @return the formatted string 
     */
    private String formatMessage(String data, String buttonName)
    {
        switch(buttonName)
        {
        case ButtonNames.WATER_DEPTH:
            return data + " meters deep";
        case ButtonNames.WIND_SPEED:
            return data + " meters per second";
        case ButtonNames.WIND_DIRECTION:
            return data + " degrees";
        case ButtonNames.COMPASS_HEADING:
            return data + " degrees";
        case ButtonNames.BOAT_SPEED:
            return data + " meters per second";
        
        default:
            // Should not reach here
            return "Cannot format Message: " + data + " " + buttonName;
        }
        
    }
    
    
    
}

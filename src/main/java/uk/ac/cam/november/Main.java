package uk.ac.cam.november;

import uk.ac.cam.november.buttons.ButtonsListener;

/**
 * Hello world!
 *
 */
public class Main 
{
	
    public static void main( String[] args )
    {
        //Start up interface to buttons
        @SuppressWarnings("unused")
        ButtonsListener buttonsListener = new ButtonsListener();
        
        //TODO: Start up NMEA reading from CANbus
        
        //TODO: Start up StateDecoder
        
        //TODO: Start up Alert Generator
        
        //TODO: Start up text to speech (maybe not depending on how interrupts work).
    }
}

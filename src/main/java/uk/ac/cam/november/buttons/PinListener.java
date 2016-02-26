package uk.ac.cam.november.buttons;

import uk.ac.cam.november.messages.MessageFormatter;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

/**
 * This class is an event listener for ONE button, which handles button clicks.
 * When the state of a button transitions from "not pressed" to "pressed",
 * the class will call the message generator method.
 *
 * @author Marius Latinis
 */

public class PinListener implements GpioPinListenerDigital {

    private static final int DEBOUNCE_TIME = 30;  // 30 miliseconds

    /** Corresponds to the button that this instance of class is listening to */
    final private String buttonName;

    public PinListener(final String buttonNameInput) {
        buttonName = buttonNameInput;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {

        if (event.getState().isHigh()) {
            
            /** Will sleep for a short time to debounce the button */
	    try {
		Thread.sleep(DEBOUNCE_TIME);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

            if (event.getState().isHigh()) {
                System.out.println(buttonName + " button has just been pressed.");
                MessageFormatter.handleButtonPress(buttonName);
            }
        }

    }

}

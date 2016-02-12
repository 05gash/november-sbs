// This class listens to all buttons.
// For every button connected to the Raspberry Pi,
// an GpioPinDigitalInput object is created.
// Each object waits for a button to be clicked,
// and calls the PinListener to deal with the click.

package uk.ac.cam.november.buttons;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;


public class ButtonsListener {

	private final static int NUMBER_OF_BUTTONS = 50;  // The actual number is much smaller
	private final GpioPinDigitalInput buttons[] = new GpioPinDigitalInput[NUMBER_OF_BUTTONS];
	private final GpioController gpio = GpioFactory.getInstance();

	public ButtonsListener() {
		buttons[7] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07,
			PinPullResistance.PULL_DOWN);
		buttons[7].addListener(new PinListener(ButtonNames.WATER_DEPTH));

		buttons[0] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_00,
			PinPullResistance.PULL_DOWN);
		buttons[0].addListener(new PinListener(ButtonNames.WIND_SPEED));

		buttons[1] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01,
            		PinPullResistance.PULL_DOWN);
        	buttons[1].addListener(new PinListener(ButtonNames.WIND_DIRECTION));

		buttons[2] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02,
            		PinPullResistance.PULL_DOWN);
        	buttons[2].addListener(new PinListener(ButtonNames.BOAT_SPEED));

		buttons[3] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03,
            		PinPullResistance.PULL_DOWN);
        	buttons[3].addListener(new PinListener(ButtonNames.COMPASS_HEADING));

		buttons[4] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04,
			PinPullResistance.PULL_DOWN);
		buttons[4].addListener(new PinListener(ButtonNames.SHUT_DOWN));

		buttons[5] = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05,
			PinPullResistance.PULL_DOWN);
		buttons[5].addListener(new PinListener(ButtonNames.NEAREST_PORT));
    	}

}

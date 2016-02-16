package uk.ac.cam.november.buttons;

// Our team decided to test whether standard pi4j library works correctly.
// Untrust came from hearing two audio messages being printed together
// after clicking two buttons at once. That sometimes happen evens in
// the presence of locking.

// This test mainly tries to check whether the buttons are debounced
// and at what rate.


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;


class GpioListenerTest {

/*

	public static void main(final String[] args) {
	
		final Thread listener = new Thread() {
			public void run() {
	        		final GpioController gpio = GpioFactory.getInstance();
	
	        		// 29 is last GPIO general purpose pin, which will not be used by other libraries.
        			final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, PinPullResistance.PULL_DOWN);

        			// create and register gpio pin listener
        			button.addListener(new GpioPinListenerDigital() {
            				@Override
            				public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
           	 				// TODO(ml693): write appropriate handle, which would aid in testing
					}
            
       		 		});

				try {
					Thread.sleep(LONG_TIME);
				} catch (InterruptedException exception) {
					exception.printStackTrace();
				}
			}
		};
		listener.setDaemon(true);
       
		final Thread tester = new Thread() {
			public void run() {




			}


		}

 
	}	

*/
}





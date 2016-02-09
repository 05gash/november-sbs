// This is the main class of the whole system.
// The method "main" of this class will be activated on boot.

package uk.ac.cam.november.boot;

import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.logging.LogConfig;
import uk.ac.cam.november.messages.SpeechSynthesis;

class Boot {

	public static final int A_LOT_OF_TIME = 1000000000;

	public static void main(final String[] args) {
		SpeechSynthesis.play("BootingUp");	
		
		LogConfig.setup();

		// TODO(ml693): after message "BootingUp" is loudly said,
		// it takes a few seconds for the system to boot
		// The system not instantly starts reacting to the buttons.
		// Need to figure out why the system is so slow.

		while (true) {
			try {	
				// Creating a class that will listen to the buttons being clicked.
				final ButtonsListener buttonsListener = new ButtonsListener();
				
				// TODO(ml693): Instantiate other modules HERE.

				for (;;) {
					Thread.sleep(A_LOT_OF_TIME);
				}

			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}	
}

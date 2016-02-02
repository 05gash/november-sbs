// This is the main class of the whole system.
// The method "main" of this class will be activated on boot.

package uk.ac.cam.november.boot;

import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.messages.SpeechSynthesis;

class Boot {

	public static void main(final String[] args) {
		SpeechSynthesis.play("BootingUp");	

		// TODO(ml693): after message "BootingUp" is loudly said,
		// it takes a few seconds for the system to boot
		// The system not instantly starts reacting to the buttons.
		// Need to figure out why the system is so slow.
	
		// Creating a class that will listen to the buttons being clicked.
		final ButtonsListener buttonsListener = new ButtonsListener();
	
		// Creating a system which will be able to recover from crashes.
		final Recovery recovery = new Recovery();

		// Running the buttonsListener within the scope of auto-crash-recovery.
		// This invoked method will execute forever.
		recovery.runAndRecover(buttonsListener);
	}	
}

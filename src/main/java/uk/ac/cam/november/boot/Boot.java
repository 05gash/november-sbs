// This is the main class of the whole system.
// The method "main" of this class will be activated on boot.

package uk.ac.cam.november.boot;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.EvictingQueue;

import uk.ac.cam.november.alerts.AlertGenerator;
import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.decoder.MessageDecoder;
import uk.ac.cam.november.input.CanBoatFacade;
import uk.ac.cam.november.logging.LogConfig;
import uk.ac.cam.november.messages.MessageFormatter;
import uk.ac.cam.november.messages.SpeechSynthesis;
import uk.ac.cam.november.simulation.Simulator;

class Boot {

	public static final int A_LOT_OF_TIME = 1000000000;
	
	public static final boolean SIMULATOR = true;

	public static void main(final String[] args) {
		SpeechSynthesis.play("BootingUp");	
		
	//	LogConfig.setup();

		// TODO(ml693): after message "BootingUp" is loudly said,
		// it takes a few seconds for the system to boot
		// The system not instantly starts reacting to the buttons.
		// Need to figure out why the system is so slow.
		
		while (true) {
			try {	
				// Creating a class that will listen to the buttons being clicked.
				final ButtonsListener buttonsListener = new ButtonsListener();
				
				// TODO(ml693): Instantiate other modules HERE.
				
				MessageDecoder messageDec = null;
				
				if (SIMULATOR){
				    Simulator sim = new Simulator();
				    sim.showUI();
				    messageDec = new MessageDecoder(sim.getMessageQueue());
				    sim.getThread().start();
				}else{
				    CanBoatFacade canboat = new CanBoatFacade(CanBoatFacade.MOCKBOAT_OPTION);
				    messageDec = new MessageDecoder(canboat.getPacketQueue());
				}
				
				AlertGenerator alertGen = new AlertGenerator();
				alertGen.setMd(messageDec);
				
				MessageFormatter.setDecoder(messageDec);
				
				Thread decoderThread = new Thread(messageDec);
				decoderThread.start();
				

				for (;;) {
					Thread.sleep(A_LOT_OF_TIME);
				}

			} catch (Exception exception) {
			    Logger l = Logger.getLogger("uk.ac.cam.november.boot");
			    l.log(Level.SEVERE, "System Crashed", exception);
				exception.printStackTrace();
			}
		}
	}	
}

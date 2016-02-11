// This is the main class of the whole system.
// The method "main" of this class will be activated on boot.

package uk.ac.cam.november.boot;

import uk.ac.cam.november.alerts.AlertGenerator;
import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.decoder.MessageDecoder;
import uk.ac.cam.november.input.CanBoatFacade;
import uk.ac.cam.november.logging.LogConfig;
import uk.ac.cam.november.messages.MessageFormatter;
import uk.ac.cam.november.messages.SpeechSynthesis;
import uk.ac.cam.november.simulation.Simulator;
import uk.ac.cam.november.simulation.network.SimulatorServer;

class Boot {

    public static final int A_LOT_OF_TIME = 1000000000;

    public static void main(final String[] args) throws Exception {

	// Making sure that if one thread crashes,
	// then the whole JVM will shut down.
	Thread.setDefaultUncaughtExceptionHandler(new Thread.
   		UncaughtExceptionHandler() {
   			public void uncaughtException(Thread t, Throwable e) {
   				System.out.println(t + " throws exception: " + e);
				System.exit(1);
  			}
   		});

        boolean runSimServer = false;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("simulator")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("client")) {
                        if (args.length == 3) {
                            // Launch the simulator client and return
                            Simulator sim = new Simulator(args[2]);
                            sim.showUI();
                            sim.getThread().start();
                            return;
                        } else {
                            System.err.println("Usage: sbs simulator client <server_address>");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Usage: sbs simulator [client <server_address>]");
                        System.exit(1);
                    }
                } else {
                    runSimServer = true;
                }
            } else {
                System.err.println("Usage: sbs [simulator [client <server_address>]]");
                System.exit(1);
            }
        }

        SpeechSynthesis.play("Booting Up");

        LogConfig.setup();

        // TODO(ml693): after message "BootingUp" is loudly said,
        // it takes a few seconds for the system to boot
        // The system not instantly starts reacting to the buttons.
        // Need to figure out why the system is so slow.

        // Creating a class that will listen to the buttons being clicked.
        try {
            new ButtonsListener();
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load Pi4J library.");
            System.err.println("Most likely you're not on a RaspberryPi.");
        }
        MessageDecoder messageDec = null;

        if (runSimServer) {
            SimulatorServer sim = new SimulatorServer();
            messageDec = new MessageDecoder(sim.getMessageQueue());
        } else {
            CanBoatFacade canboat = new CanBoatFacade(CanBoatFacade.MOCKBOAT_OPTION);
            messageDec = new MessageDecoder(canboat.getPacketQueue());
        }

        AlertGenerator alertGen = new AlertGenerator();
        alertGen.setMd(messageDec);

        MessageFormatter.setDecoder(messageDec);

        Thread decoderThread = new Thread(messageDec, "Message-Decoder");
        decoderThread.start();

        for (;;) {
            Thread.sleep(A_LOT_OF_TIME);
        }
    }
}

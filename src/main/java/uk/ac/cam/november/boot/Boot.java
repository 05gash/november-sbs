package uk.ac.cam.november.boot;

import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.decoder.AlertHandler;
import uk.ac.cam.november.decoder.MessageDecoder;
import uk.ac.cam.november.input.CanBoatFacade;
import uk.ac.cam.november.logging.LogConfig;
import uk.ac.cam.november.messages.MessageFormatter;
import uk.ac.cam.november.simulation.Simulator;
import uk.ac.cam.november.simulation.network.SimulatorServer;

/**
 * This is the main class for the whole system. The class listens to the 
 * buttons being pressed and starts running the simulator of data, the 
 * message decoder, and the alert handler. The main method of the class 
 * is activated on boot.
 *
 * @author
 */

class Boot {

    public static final int A_LOT_OF_TIME = 1000000000;

    private static boolean suppliedCoordinatesCorrect(final float latitude, final float longtitude) {
        return (latitude >= -89.0 && latitude <= 89.0 && longtitude >= -179.0 && longtitude <= 179.0);
    }


    public static void main(final String[] args) throws Exception {

        /** If one thread crashes, the whole JVM will shut down */
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println(t + " throws exception: " + e);
                e.printStackTrace();
                System.exit(1);
            }
        });

        boolean runSimServer = false;

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("simulator")) {
                if (args.length > 1) {
                    if (args[1].equalsIgnoreCase("client")) {
                        if (args.length == 3 || args.length == 5) {
                            // If latitude and longtitude arguments have been supplied,
                            // we will start boat on the location specified by those arguments.
                            // Otherwise, the boat will start on (0, 0).
                            final boolean coordinatesSupplied = args.length == 5 ? true : false;
                            final float latitude = coordinatesSupplied ? Float.parseFloat(args[3]) : 0;
			    final float longtitude = coordinatesSupplied ? Float.parseFloat(args[4]) : 0;
                            if (suppliedCoordinatesCorrect(latitude, longtitude)) {
                                /** Launch the simulator client and return */
                                Simulator sim = new Simulator(args[2], latitude, longtitude);
                                sim.showUI();
                                sim.getThread().start();
                                return;
                            } else {
                                System.err.println("Coordinates out of range: (-90.0, 90.0) for latitude, (-180.0, 180.0) for longtitude");
                                System.exit(1);
                            }
                        } else {
                            System.err.println("Usage: sbs simulator client <server_address> [optional - latitude] [optional - longtitude]");
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

        LogConfig.setup();
        ScriptCreator.writeScripts();
        
        /** Listens to buttons */
        try {
            new ButtonsListener();
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Failed to load Pi4J library.");
            System.err.println("Most likely you're not on a RaspberryPi.");
            return;
        }
        MessageDecoder messageDec = null;

        /** Initializes a Simulator Server and a MessageDecoder */
        if (runSimServer) {
            SimulatorServer sim = new SimulatorServer();
            messageDec = new MessageDecoder(sim.getMessageQueue());
        } else {
            CanBoatFacade canboat = new CanBoatFacade(CanBoatFacade.MOCKBOAT_OPTION);
            messageDec = new MessageDecoder(canboat.getPacketQueue());
        }
        MessageFormatter.setDecoder(messageDec);

        /** Starts running the Message Decoder */
        Thread decoderThread = new Thread(messageDec, "Message-Decoder");
        decoderThread.start();

        /** Starts running the Alert System */
        AlertHandler alertHandler = new AlertHandler(messageDec);
        Thread alertThread = new Thread(alertHandler, "Alert-Handler");
        alertThread.start();

        for (;;) {
            Thread.sleep(A_LOT_OF_TIME);
        }

    }
}

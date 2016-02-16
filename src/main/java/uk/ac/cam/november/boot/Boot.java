// This is the main class of the whole system.
// The method "main" of this class will be activated on boot.

package uk.ac.cam.november.boot;

import uk.ac.cam.november.buttons.ButtonsListener;
import uk.ac.cam.november.decoder.MessageDecoder;
import uk.ac.cam.november.input.CanBoatFacade;
import uk.ac.cam.november.logging.LogConfig;
import uk.ac.cam.november.messages.MessageFormatter;
import uk.ac.cam.november.messages.SpeechSynthesis;
import uk.ac.cam.november.simulation.Simulator;
import uk.ac.cam.november.simulation.network.SimulatorServer;
import java.nio.channels.FileChannel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Boot {

    public static final int A_LOT_OF_TIME = 1000000000;

    // usual file copy method
    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    // copy resource into temp folder
    private static void copyRes(String resname, String fname) {
	// TODO(team): make this method work.
        /*
	try {
            File fin = new File(getClass().getResource("/res/" + resname).getFile());
            File fout = new File("temp/" + fname);
            copyFile(fin, fout);
        } catch (IOException e) {
            System.err.println("[Error in Boot]");
            System.err.println(" -- I/O exception raised from copyRes()");
        }
	*/
    }

    public static void main(final String[] args) throws Exception {

        // Making sure that if one thread crashes,
        // then the whole JVM will shut down.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.
                UncaughtExceptionHandler() {
                    public void uncaughtException(Thread t, Throwable e) {
                        System.out.println(t + " throws exception: " + e);
			e.printStackTrace();
                        System.exit(1);
                    }
                });

	SpeechSynthesis.play("Booting Up");

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

        MessageFormatter.setDecoder(messageDec);

        Thread decoderThread = new Thread(messageDec, "Message-Decoder");
        decoderThread.start();

        for (;;) {
            Thread.sleep(A_LOT_OF_TIME);
        }

    }
}

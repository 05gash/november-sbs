// shutDown method COMPLETELY turns the Raspberry Pi off.
// This method can be used together with a button click:
// after pressing a dedicated "Shutdown" button, 
// the operating system goes off. 

package uk.ac.cam.november.boot;

import java.lang.InterruptedException;

public class ShutDown {

        private static final Long SHUTDOWN_MESSAGE_TIME = 3000L; // 3 seconds

	public static synchronized void shutDown() {
                // As the message of system going to be shut down had been
                // anounced before, we will give some time for that
                // message to finish.
                try {
                    Thread.sleep(SHUTDOWN_MESSAGE_TIME);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }

		Runtime rt = Runtime.getRuntime();
		String[] cmd = {"sudo", "shutdown", "-h", "now"};
		try {
			Process proc = rt.exec(cmd);
		} catch (Exception exception) {
			// TODO(ml693): catch exact exception (security and IO)
			exception.printStackTrace();
		}
	}
}

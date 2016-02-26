package uk.ac.cam.november.boot;

import java.lang.InterruptedException;

/**
 * This class completely turns off the Raspberry Pie.
 * If a special "shut-down" button is pressed or if the shut-down button is
 * pressed together with some other button, the operating system goes off.
 *
 * @author
 */

public class ShutDown {

        private static final Long SHUTDOWN_MESSAGE_TIME = 3000L; // 3 seconds

	public static synchronized void shutDown() {
	            /** Leaving some time for the message of shutting down the system
	             * to be announced */
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

// shutDown method COMPLETELY turns the Raspberry Pi off.
// This method can be used together with a button click:
// after pressing a dedicated "Shutdown" button, 
// the operating system goes off. 

package uk.ac.cam.november.boot;

public class ShutDown {

	public static synchronized void shutDown() {
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

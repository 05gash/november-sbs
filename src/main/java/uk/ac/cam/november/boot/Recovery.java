// NOTE: DEPRECATED FILE, the same functionality is now implemented in
// Boot.java directly

// This is a "Javish" attemt to write a system that will be able to recover from crashes. 
// An alternative way is to write a shell script doing autorecovery.
// Have a look at the script file if you think that bash program is going to be more useful.

// The Recovery class works as follows.
// It has a method runAndRecover which takes a runnable class as an input.
// The method runs the input.
// As long as the input keeps running, the method is blocked (no crash so far).
// If the method throws an exception (crash), 
// the runAndRecover method becomes unblocked (attempt to recover activated).
// When the method is unblocked, it runs the input again (crash recovery).

package uk.ac.cam.november.boot;

import java.util.logging.Level;
import java.util.logging.Logger;

class Recovery {

	final Object lock = new Object();

	void runAndRecover(final Runnable runnable) {
		
		while (true) {
			// Synchronising to make sure that only one instance
			// of runnable input is running at the time.
			synchronized(lock) {
				try {
					runnable.run();
					// Ideally the process should run forever (no termination),
					// and the code of THIS FILE will be blocked HERE
					// with the runnable RUNNING (which is what we want).

				} catch (Exception exception) {
					// If the process was interrupted by exception
					// we will restart it.
				    Logger l = Logger.getLogger("uk.ac.cam.november.Recovery");
				    l.log(Level.SEVERE, "System Crashed!", exception);
					lock.notifyAll();

					// TODO(ml693): currently not printing stack trace - 
					// exception.printStackTrace() -
					// because it complicates RecoveryTest file.
					// Need to incorporate this method.
				}
			}	
		}		

	}

}

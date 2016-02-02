// TODO(ml693): rewrite test as a JUnit module

package uk.ac.cam.november.boot;

class RecoveryTest {

	private static int SOME_TIME_TO_WAIT = 1000; // Corresponds to 1 second


	public static void main(final String args[]) {

		// 1) Creating a class which always throws an exception
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				final Runnable exceptionThrower = new Runnable() {
					@Override
					public void run() {
						// TODO(ml693): create another test case
						// in which another type of exception would be thrown,
						// such as:
						// int[] array = new int[5];
						// System.out.println("Line + array[6] + " should not get printed");
	
						int x = 0;
						int y = 0;
						// Throws DivisionByZero exception.
						int z = x / y;
					}
				};
			
				final Recovery recovery = new Recovery();
				recovery.runAndRecover(exceptionThrower);
			}

		};

		// 2) Creating a thread which ideally should run forever
		// by restarting even if the exception is thrown.
		Thread everlastingThread = new Thread(runnable);
		everlastingThread.setDaemon(true);
	
		// Will now run the previously created thread for a few seconds.
		// If it does not crash itself, then we assume that
		// the test has passed.
		everlastingThread.start();

		long endTime = System.currentTimeMillis() + SOME_TIME_TO_WAIT;
		while (System.currentTimeMillis() < endTime) {} // Waiting for some time

		if (everlastingThread.isInterrupted()) {
			System.out.println("Test failed");
		} else {
			System.out.println("Test succesfully completed");
		}
	}	
}

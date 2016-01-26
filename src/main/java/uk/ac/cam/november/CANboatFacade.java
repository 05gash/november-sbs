package uk.ac.cam.november;

import java.io.IOException;
import java.io.OutputStream;

public class CANboatFacade {
	
	private ProcessBuilder processBuilder;
	private Process canBoatProcess;
	private static final String CANBOAT_COMMAND = "candump2anaylser";

	public CANboatFacade(){
		processBuilder = new ProcessBuilder(CANBOAT_COMMAND);
		processBuilder.redirectErrorStream(true);
		try {
			canBoatProcess = processBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public OutputStream getMessageQueue(){
		return canBoatProcess.getOutputStream();
	}
}

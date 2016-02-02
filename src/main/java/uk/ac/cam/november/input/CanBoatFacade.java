package uk.ac.cam.november.input;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

public class CanBoatFacade {

    CommandLine canBoatCommandLine;
    DefaultExecutor canBoatExecutor;
    MessageLogOutputStream canboatOut;
    PumpStreamHandler canPump; 
    DefaultExecuteResultHandler resultHandler;
    private static final String CANBOAT_COMMAND = "src/main/bash/canScript.sh";

    public CanBoatFacade() throws ExecuteException, IOException {
        canBoatCommandLine = CommandLine.parse(CANBOAT_COMMAND);
        canboatOut = new MessageLogOutputStream();
        canPump = new PumpStreamHandler(canboatOut, System.err);
        resultHandler = new DefaultExecuteResultHandler();
    }

    
    public void startCanBoat() throws ExecuteException, IOException {
        canBoatExecutor = new DefaultExecutor();
        canBoatExecutor.setStreamHandler(canPump);
        canBoatExecutor.execute(canBoatCommandLine, resultHandler);
    }
}

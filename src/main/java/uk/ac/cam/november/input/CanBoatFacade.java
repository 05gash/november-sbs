package uk.ac.cam.november.input;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import com.google.common.collect.EvictingQueue;

import uk.ac.cam.november.message.Message;

public class CanBoatFacade {

    private CommandLine canBoatCommandLine;
    private DefaultExecutor canBoatExecutor;
    private MessageLogOutputStream canboatOut;
    private PumpStreamHandler canPump; 
    private DefaultExecuteResultHandler resultHandler;
    private static final String CANBOAT_COMMAND = "src/main/bash/canScript.sh";
    
    /**
     * Creates an interface to CANboat
     * @throws ExecuteException
     * @throws IOException
     */

    public CanBoatFacade() throws ExecuteException, IOException {
        canBoatCommandLine = CommandLine.parse(CANBOAT_COMMAND);
        canboatOut = new MessageLogOutputStream();
        canPump = new PumpStreamHandler(canboatOut,System.err);
        resultHandler = new DefaultExecuteResultHandler();
    }

    /**
     * Starts CANboat script
     * @throws ExecuteException
     * @throws IOException
     */
    
    public void startCanBoat() throws ExecuteException, IOException {
        canBoatExecutor = new DefaultExecutor();
        canBoatExecutor.setStreamHandler(canPump);
        canBoatExecutor.execute(canBoatCommandLine, resultHandler);
    }
    
    /**
     * returns an evicting queue of Messages size 300 
     * @return
     */
    public EvictingQueue<Message> getMessageQueue(){
        return canboatOut.getMessageQueue();
    }
}

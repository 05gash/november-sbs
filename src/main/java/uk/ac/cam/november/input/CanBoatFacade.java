package uk.ac.cam.november.input;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;

import com.google.common.collect.EvictingQueue;

import uk.ac.cam.november.packet.Packet;

public class CanBoatFacade {

    private CommandLine canBoatCommandLine;
    private DefaultExecutor canBoatExecutor;
    private MessageLogOutputStream canboatOut;
    private PumpStreamHandler canPump; 
    private DefaultExecuteResultHandler resultHandler;
    private static final String CANBOAT_COMMAND = "src/main/bash/canScript.sh";
    public static final String DEFAULT_OPTION = "-b";
//    public static final String SIMULATOR_OPTION = "-s";
    public static final String DATAGEN_OPTION = "-d";
    
    /**
     * Creates an interface to CANboat
     * @throws ExecuteException
     * @throws IOException
     */

    public CanBoatFacade(String option) throws ExecuteException, IOException {
        canBoatCommandLine = CommandLine.parse(CANBOAT_COMMAND);
        if(option != null){
            canBoatCommandLine.addArgument(option);
        }else{
            canBoatCommandLine.addArgument(DEFAULT_OPTION);
        }
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
    public EvictingQueue<Packet> getMessageQueue(){
        return canboatOut.getMessageQueue();
    }
}

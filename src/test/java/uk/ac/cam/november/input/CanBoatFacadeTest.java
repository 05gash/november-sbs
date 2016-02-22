package uk.ac.cam.november.input;

import java.io.IOException;
import java.util.Queue;

import org.apache.commons.exec.ExecuteException;
import org.junit.Test;

import uk.ac.cam.november.packet.Packet;

public class CanBoatFacadeTest {

    @Test
    public void shouldReceiveMessages() throws ExecuteException, IOException, InterruptedException {
        CanBoatFacade canBoatFacade = new CanBoatFacade(CanBoatFacade.DATAGEN_OPTION);
        canBoatFacade.startCanBoat();
        Queue<Packet> packetQueue = canBoatFacade.getPacketQueue();
        for(int i=0; i<50; i++){
            Packet packet = packetQueue.poll();
            if(packet != null){
                System.out.println(packet.getFields().getDeviation());
            }
            
            Thread.sleep(20);
        }
    }
}

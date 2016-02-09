package uk.ac.cam.november.input;

import java.io.IOException;
import java.util.Queue;

import org.apache.commons.exec.ExecuteException;

import uk.ac.cam.november.packet.Packet;

public class CanBoatFacadething {

    public static void main(String[] args) throws ExecuteException, IOException, InterruptedException {
        CanBoatFacade canBoatFacade = new CanBoatFacade(CanBoatFacade.DATAGEN_OPTION);
        canBoatFacade.startCanBoat();
        Queue<Packet> packetQueue = canBoatFacade.getPacketQueue();
        while(true){
            Packet packet = packetQueue.poll();
            if(packet != null){
                System.out.println(packet.getFields().getDeviation());
            }
            
            Thread.sleep(40);
        }
    }
}

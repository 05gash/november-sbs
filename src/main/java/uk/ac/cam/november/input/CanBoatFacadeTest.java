package uk.ac.cam.november.input;

import java.io.IOException;

import org.apache.commons.exec.ExecuteException;

import com.google.common.collect.EvictingQueue;

import uk.ac.cam.november.message.Message;

public class CanBoatFacadeTest {

    public static void main(String[] args) throws ExecuteException, IOException, InterruptedException {

        CanBoatFacade canBoatFacade = new CanBoatFacade();
        canBoatFacade.startCanBoat();
        EvictingQueue<Message> messageQueue = canBoatFacade.canboatOut.getMessageQueue();
        while (true) {
            Message message = messageQueue.poll();
            if(message != null){
                System.out.println(message);
            }
            Thread.sleep(100);
        }
    }
}

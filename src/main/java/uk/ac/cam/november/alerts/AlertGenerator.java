package uk.ac.cam.november.alerts;

import java.util.Queue;

import uk.ac.cam.november.decoder.BoatState;
import uk.ac.cam.november.decoder.MessageDecoder;

public class AlertGenerator {

    AlertMessage am = new AlertMessage();
    Queue<AlertMessage> AlertMessageQueue;
    
    MessageDecoder messageDec = new MessageDecoder();
    BoatState state = null;
   
    public void setMd(MessageDecoder md) {
        this.messageDec = messageDec;
        state = messageDec.getState();
    }
    

    
}

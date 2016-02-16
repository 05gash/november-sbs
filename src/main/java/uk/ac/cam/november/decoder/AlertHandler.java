package uk.ac.cam.november.decoder;

import java.util.Queue;

import uk.ac.cam.november.messages.MessageFormatter;

public class AlertHandler implements Runnable {

    private MessageDecoder mdecoder;
    
    public AlertHandler(MessageDecoder decoder)
    {
        mdecoder = decoder;
    }
    
    @Override
    public void run() {
        Queue<AlertMessage> alertQueue = mdecoder.getAlertMessageQueue();
        while(true)
        {
            AlertMessage alert = alertQueue.poll();
            
            if(alert == null)
            {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
                
            MessageFormatter.handleAlert(alert);
        }
        
    }
    
}

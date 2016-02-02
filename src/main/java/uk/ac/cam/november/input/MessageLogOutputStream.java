package uk.ac.cam.november.input;

import org.apache.commons.exec.LogOutputStream;

import com.google.common.collect.EvictingQueue;
import com.google.gson.Gson;

import uk.ac.cam.november.message.Message;

public class MessageLogOutputStream extends LogOutputStream{
    
    Gson gson = new Gson();
    EvictingQueue<Message> messageQueue = EvictingQueue.create(300);
    
    @Override
    protected void processLine(String arg0, int level) {
        Message message = gson.fromJson(arg0, Message.class);
        messageQueue.add(message);
    }

    
    EvictingQueue<Message> getMessageQueue(){
        return messageQueue;
    }
}

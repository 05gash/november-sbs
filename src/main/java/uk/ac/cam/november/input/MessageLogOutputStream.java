package uk.ac.cam.november.input;

import org.apache.commons.exec.LogOutputStream;

import com.google.common.collect.EvictingQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.cam.november.message.Message;

public class MessageLogOutputStream extends LogOutputStream{
    
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd-HH:mm:ss.SSSSS").create();
    EvictingQueue<Message> messageQueue = EvictingQueue.create(300);
    
    @Override
    public void processLine(String arg0, int level) {
        Message message = gson.fromJson(arg0, Message.class);
        messageQueue.add(message);
    }
    
    public EvictingQueue<Message> getMessageQueue(){
        return messageQueue;
    }
}

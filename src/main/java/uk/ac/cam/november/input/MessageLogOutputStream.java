package uk.ac.cam.november.input;

import org.apache.commons.exec.LogOutputStream;

import com.google.common.collect.EvictingQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.cam.november.packet.Packet;

public class MessageLogOutputStream extends LogOutputStream{
    
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd-HH:mm:ss.SSSSS").create();
    EvictingQueue<Packet> messageQueue = EvictingQueue.create(300);
    
    @Override
    public void processLine(String arg0, int level) {
        Packet message = gson.fromJson(arg0, Packet.class);
        messageQueue.add(message);
    }
    
    protected EvictingQueue<Packet> getMessageQueue(){
        return messageQueue;
    }
}

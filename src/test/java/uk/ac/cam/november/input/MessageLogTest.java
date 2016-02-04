package uk.ac.cam.november.input;

import org.junit.Test;
import static org.junit.Assert.*;

import uk.ac.cam.november.input.MessageLogOutputStream;
import uk.ac.cam.november.packet.Packet;

public class MessageLogTest {
    private MessageLogOutputStream messageLogOutputStream = new MessageLogOutputStream();
    private static final String HEADING_DATA_JSON = "{\"timestamp\":\"2016-02-02-15:21:25.204385\",\"prio\":2,\"src\":36,\"dst\":255,\"pgn\":127250,\"description\":\"Vessel Heading\",\"fields\":{\"SID\":0,\"Heading\":182.4,\"Deviation\":0.0,\"Variation\":0.0,\"Reference\":\"Magnetic\"}}";
    private static final String SPEED_DATA_JSON = "";
    
    @Test
    public void processLineReturnsForValidJson(){
        messageLogOutputStream.processLine(HEADING_DATA_JSON, 0);
        Packet message = messageLogOutputStream.getMessageQueue().poll();
        assertNotNull(message);
        assertEquals(message.getDescription(), "Vessel Heading");
        assertEquals(message.getFields().getVariation(), 0.0, 0.01);
        assertEquals(message.getFields().getDeviation(), 0.0, 0.01);
        assertEquals(message.getFields().getHeading(), 182.4, 0.01);
    }
    
    @Test
    public void processLineReturnsValidSpeedData(){
        
    }
    

}

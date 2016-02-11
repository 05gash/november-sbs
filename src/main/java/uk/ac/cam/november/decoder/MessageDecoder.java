package uk.ac.cam.november.decoder;

import java.util.Queue;

import uk.ac.cam.november.packet.Fields;
import uk.ac.cam.november.packet.Packet;

public class MessageDecoder implements Runnable {
    
    int criticalMinDepth = 15;
    int criticalMaxDepth = 1000;
    int criticalChangeDepth = 50;
    
    int criticalMinWindSpeed = 0;
    int criticalMaxWindSpeed = 0;
    int criticalChangeWindSpeed = 0;
    int criticalChangeWindAngle = 0;
    
    int criticalChangeHeading = 0;
    
    int criticalMinBoatSpeed = 0;
    int criticalMaxBoatSpeed = 0;
    int criticalChangeSpeed = 0;

    
    /*
     * Water Depth "128267"
     * Wind Data "130306"  -- both Wind Direction and Wind Speed
     * heading "126720"
     * Boat Speed "128259"
     */      
    
    Queue<Packet> MessageQueue;
    Queue<AlertMessage> AlertMessageQueue;
    
    BoatState state = new BoatState();
    AlertMessage am = new AlertMessage();
    
    public MessageDecoder(Queue<Packet> messageQueue){
        this.MessageQueue = messageQueue;
    }
    
    public BoatState getState() {
        return state;
    }

    @Override
    public void run() {

        state.setDepth(0);
        state.setOffset(0);
        state.setWindSpeed(0);
        state.setWindAngle(0);
        state.setSpeedWaterReferenced(0);
        state.setHeading(0);
        state.setDeviation(0);
        state.setVariation(0);
        
        boolean first_d = true;
        boolean first_w = true;
        boolean first_h = true;
        boolean first_s = true;
        
        
        while (true) {
            Packet packet = MessageQueue.poll();
            if(packet == null){
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) { }
                continue;
            }
            int packetID = packet.getPgn();
            Fields fields = (Fields) packet.getFields();
            
            
            switch (packetID) {
      
            /** 
             * If a packet is of type WaterDepth, png = 128267
             * updates the current value of Depth and Offset
             * generates alert messages if a boat is in critical state
             */
            case 128267:
              
                if (!first_d) {
                
                    // CriticalChange
                    if (fields.getDepth() - state.getDepth() > criticalChangeDepth | 
                            state.getDepth() - state.getDepth() > criticalChangeDepth) {
                        am.setAlertType(0);
                        am.setSensor(0);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMax
                    if (fields.getDepth() > criticalMaxDepth) {
                        am.setAlertType(1);
                        am.setSensor(0);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMin
                    if (fields.getDepth() < criticalMinDepth) {
                        am.setAlertType(2);
                        am.setSensor(0);
                        AlertMessageQueue.add(am);
                    }
                }
                
                state.setDepth(fields.getDepth());
                state.setOffset(fields.getOffset());
                
                break;
            
            /** 
             * If a packet is of type WindData, png = 130306
             * updates the current value of Speed and Angle
             * generates alert messages if a boat is in critical state
             */   
            case 130306:
                if (!first_w) {
                
                    // CriticalChange in Wind Speed
                    if (fields.getWindSpeed() - state.getWindSpeed() > criticalChangeWindSpeed | 
                            state.getWindSpeed() - fields.getWindSpeed() > criticalChangeWindSpeed) {
                        am.setAlertType(0);
                        am.setSensor(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMax in WindSpeed
                    if (fields.getWindSpeed() > criticalMaxWindSpeed) {
                        am.setAlertType(1);
                        am.setSensor(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMin in WindSpeed
                    if (fields.getWindSpeed() < criticalMinWindSpeed) {
                        am.setAlertType(2);
                        am.setSensor(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalChange in Wind Angle
                    if (fields.getWindAngle() - state.getWindAngle() > criticalChangeWindAngle | 
                            state.getWindAngle() - fields.getWindAngle() > criticalChangeWindAngle) {
                        am.setAlertType(0);
                        am.setSensor(2);
                        AlertMessageQueue.add(am);
                    }
                    
                    
                }
          
                state.setWindSpeed(fields.getWindSpeed());
                state.setWindAngle(fields.getWindAngle());
                
                break;
            
            /** 
             * If a packet is of type BoatHeading, png = 127250
             * updates the current value of Heading, Deviation, and Variation
             * generates alert messages if a boat is in critical state
             */     
            case 127250:
                if (!first_h) {
                    
                    // CriticalChange in Heading
                    if (fields.getHeading() - state.getHeading() > criticalChangeHeading | 
                    state.getHeading() - fields.getHeading() > criticalChangeHeading) {
                        am.setAlertType(0);
                        am.setSensor(3);
                        AlertMessageQueue.add(am);
                    }
                }
          
                state.setHeading(fields.getHeading());
                state.setDeviation(fields.getDeviation());
                state.setVariation(fields.getVariation());
                break;
                
            /** 
             * If a packet is of type BoatSpeed, png = 128259
             * updates the current value of Speed
             * generates alert messages if a boat is in critical state
             */ 
            case 128259:
                
                if (!first_s) {
                    
                    // CriticalChange in Boat Speed
                    if (fields.getSpeedWaterReferenced() - state.getSpeedWaterReferenced() > criticalChangeSpeed | 
                            state.getSpeedWaterReferenced() - fields.getSpeedWaterReferenced() > criticalChangeSpeed) {
                        am.setAlertType(0);
                        am.setSensor(4);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMax in Boat Speed
                    if (fields.getSpeedWaterReferenced() > criticalMaxBoatSpeed) {
                        am.setAlertType(1);
                        am.setSensor(4);
                        AlertMessageQueue.add(am);
                    }
                    
                    // CriticalMin in Boat Speed
                    if (fields.getSpeedWaterReferenced() < criticalMinBoatSpeed) {
                        am.setAlertType(2);
                        am.setSensor(4);
                        AlertMessageQueue.add(am);
                    }
                }
          
                state.setSpeedWaterReferenced(fields.getSpeedWaterReferenced());
                break;
                
            default:
                    // "CANNOT DECODE A MESSAGE!"
                    break;
            }
         
        }
        
    }
    

}
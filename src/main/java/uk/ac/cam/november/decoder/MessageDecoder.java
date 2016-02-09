package uk.ac.cam.november.decoder;

import java.util.Queue;
import javax.xml.datatype.Duration;

import uk.ac.cam.november.alerts.AlertMessage;
import uk.ac.cam.november.packet.Fields;
import uk.ac.cam.november.packet.Packet;

public class MessageDecoder implements Runnable {
    
    /*
     * Example JSON input (from CAN Boat using sample data on Wiki) -- WATER DEPTH
     * 
     * {"timestamp":"2011-11-24-22:42:04.388","prio":2,"src":36,"dst":255,"pgn":127251,"description":"Rate of Turn","fields":{"SID":125,"Rate":0.029200}}
     * {"timestamp":"2011-11-24-22:42:04.390","prio":2,"src":36,"dst":255,"pgn":127250,"description":"Vessel Heading","fields":{"SID":0,"Heading":182.4,"Deviation":0.0,"Variation":0.0,"Reference":"Magnetic"}}
     * {"timestamp":"2011-11-24-22:42:04.437","prio":2,"src":36,"dst":255,"pgn":130306,"description":"Wind Data","fields":{"SID":177,"Wind Speed":0.92,"Wind Angle":353.4,"Reference":"Apparent"}}
     * 
     * Water Depth "128267"
     * Wind Data "130306"  -- both Wind Direction and Wind Speed
     * heading "126720"
     * Boat Speed "128259"
     */      
    

    
//    final Duration timeout = Duration.ofSeconds(10);
    
    Queue<Packet> MessageQueue;
    Queue<AlertMessage> AlertMessageQueue;
    
    BoatState state = new BoatState();
    AlertMessage am = new AlertMessage();
    
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
            int packetID = packet.getPgn();
            Fields fields = (Fields) packet.getFields();
            
            
            switch (packetID) {
      
            /** 
             * If a packet is of type WaterDepth, png = 128267
             * updates the current value of Depth and Offset
             * generates alert messages if a boat is in critical state
             */
            case 128267:
                
                // Alerts`
                if (!first_d) {
                
                    // Rapid Change
                    if (fields.getDepth() - state.getDepth() > 20 | state.getDepth() - state.getDepth() > 20) {
                        am.setType(0);
                        am.setWhat(0);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Critical Value
                    if (fields.getDepth() > 1000) {
                        am.setType(1);
                        am.setWhat(0);
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
                
                    // Rapid Change in Wind Speed
                    if (fields.getWindSpeed() - state.getWindSpeed() > 20 | 
                            state.getWindSpeed() - fields.getWindSpeed() > 20) {
                        am.setType(0);
                        am.setWhat(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Rapid Change in Wind Angle
                    if (fields.getWindAngle() - state.getWindAngle() > 30 | 
                            state.getWindAngle() - fields.getWindAngle() > 30) {
                        am.setType(0);
                        am.setWhat(2);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Critical Value in WindSpeed
                    if (fields.getWindSpeed() > 30) {
                        am.setType(1);
                        am.setWhat(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Critical Value in WindAngle
                    if (fields.getWindAngle() > 50) {
                        am.setType(1);
                        am.setWhat(2);
                        AlertMessageQueue.add(am);
                    }
                    
                }
          
                state.setWindSpeed(fields.getWindSpeed());
                state.setWindAngle(fields.getWindAngle());
                
                break;
            
            /** 
             * If a packet is of type BoatHeading, png = 126720
             * updates the current value of Heading, Deviation, and Variation
             * generates alert messages if a boat is in critical state
             */     
            case 126720:
                
                // Alerts
                if (!first_h) {
                    
                    // Rapid Change
                    if (fields.getHeading() - state.getHeading() > 20 | state.getHeading() - fields.getHeading() > 20) {
                        am.setType(0);
                        am.setWhat(3);
                        AlertMessageQueue.add(am);
                    }
                    
                    // No Critical Value Alert
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
                    
                    // Rapid Change
                    if (fields.getSpeedWaterReferenced() - state.getSpeedWaterReferenced() > 20 | 
                            state.getSpeedWaterReferenced() - fields.getSpeedWaterReferenced() > 20) {
                        am.setType(0);
                        am.setWhat(4);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Critical Value
                    if (fields.getSpeedWaterReferenced() > 50) {
                        am.setType(1);
                        am.setWhat(4);
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
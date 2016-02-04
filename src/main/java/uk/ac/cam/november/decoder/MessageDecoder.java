package uk.ac.cam.november.decoder;

import java.util.Queue;
import javax.xml.datatype.Duration;

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
    
    AlertMessage am = new AlertMessage();

    Queue<Packet> MessageQueue;
    Queue<AlertMessage> AlertMessageQueue;
//    final Duration timeout = Duration.ofSeconds(10);
    
    Fields c_fields = new Fields();

    @Override
    public void run() {

        c_fields.setDepth(0);
        c_fields.setOffset(0);
        c_fields.setWindSpeed(0);
        c_fields.setWindAngle(0);
        c_fields.setSpeedWaterReferenced(0);
        c_fields.setHeading(0);
        c_fields.setDeviation(0);
        c_fields.setVariation(0);
        
        boolean first_d = true;
        boolean first_w = true;
        boolean first_h = true;
        boolean first_s = true;
        
        
        while (true) {
            Packet packet = MessageQueue.poll();
            int messageID = packet.getPgn();
            Fields fields = (Fields) packet.getFields();
            
            
            switch (messageID) {
      
            /** 
             * If a packet is of type WaterDepth, png = 128267
             * updates the current value of Depth and Offset
             * generates alert messages if a boat is in critical state
             */
            case 128267:
                
                // Alerts
                if (!first_d) {
                
                    // Rapid Change
                    if (fields.getDepth() - c_fields.getDepth() > 20 | c_fields.getDepth() - fields.getDepth() > 20) {
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
                
                c_fields.setDepth(fields.getDepth());
                c_fields.setOffset(fields.getOffset());
                
                break;
            
            /** 
             * If a packet is of type WindData, png = 130306
             * updates the current value of Speed and Angle
             * generates alert messages if a boat is in critical state
             */   
            case 130306:
                if (!first_w) {
                
                    // Rapid Change in Wind Speed
                    if (fields.getWindSpeed() - c_fields.getWindSpeed() > 20 | 
                            c_fields.getWindSpeed() - fields.getWindSpeed() > 20) {
                        am.setType(0);
                        am.setWhat(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    // Rapid Change in Wind Angle
                    if (fields.getWindAngle() - c_fields.getWindAngle() > 30 | 
                            c_fields.getWindAngle() - fields.getWindAngle() > 30) {
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
          
                c_fields.setWindSpeed(fields.getWindSpeed());
                c_fields.setWindAngle(fields.getWindAngle());
                
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
                    if (fields.getHeading() - c_fields.getHeading() > 20 | c_fields.getHeading() - fields.getHeading() > 20) {
                        am.setType(0);
                        am.setWhat(3);
                        AlertMessageQueue.add(am);
                    }
                    
                    // No Critical Value Alert
                }
          
                c_fields.setHeading(fields.getHeading());
                c_fields.setDeviation(fields.getDeviation());
                c_fields.setVariation(fields.getVariation());
                break;
                
            /** 
             * If a packet is of type BoatSpeed, png = 128259
             * updates the current value of Speed
             * generates alert messages if a boat is in critical state
             */ 
            case 128259:
                if (!first_s) {
                    
                    // Rapid Change
                    if (fields.getSpeedWaterReferenced() - c_fields.getSpeedWaterReferenced() > 20 | 
                            c_fields.getSpeedWaterReferenced() - fields.getSpeedWaterReferenced() > 20) {
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
          
                c_fields.setSpeedWaterReferenced(fields.getSpeedWaterReferenced());
                break;
                
            default:
                    // "CANNOT DECODE A MESSAGE!"
                    break;
            }
         
        }
        
    }
    

}
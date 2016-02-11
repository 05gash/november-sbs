package uk.ac.cam.november.decoder;

import java.util.Queue;

import uk.ac.cam.november.packet.Fields;
import uk.ac.cam.november.packet.Packet;

/**
 * This class initializes the current Boat State, composed of values of:
 * --> From Water Depth Sensor: Water Depth and WaterDepthOffset;
 * --> From Wind Sensor: WindSpeed and WindAngle;
 * --> From Boat Speed Sensor: BoatSpeedReferenced; 
 * --> From Boat Heading Sensor: BoatHeading, BoatHeadingDerivation, BoatHeadingVariation.
 * 
 * Receives decoded NMEA packets and updates the current Boat State
 * 
 * Generates 4 types of alerts and puts them onto AlertMessageQueue:
 * --> type0: the state of the boat has critically changed: CriticalChange alert;
 * --> type1: a value of the state of the boat is critically large: CriticalMax value alert;
 * --> type2: a value of the state of the boat is critically small: CriticalMin value alert;
 * --> type4: the module stopped receiving packets for some time (stale data): TimeOut alert.
 * 
 * @author Marie Menshova
 *
 */

public class MessageDecoder implements Runnable {
    
    /** Values describing critical state of the system */
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

        /** Initial values of the boat state are zeros */
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
            /** Receive a packet from NMEA input */
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
             * If a packet is of type WaterDepth, png = 128267, 
             * generates Alert Messages and puts them on AlertMessageQueue, of types: 
             * no data (timeout), criticalChange, criticalMax or criticalMin values
             * updates the current value of Speed and Angle
             */ 
            case 128267:
                
                /** If it is the first message of this type, NO criticalChange alert */
                if (!first_d) {
                
                    /** Generates an alert of type CriticalChange in Water Depth */
                    if (fields.getDepth() - state.getDepth() > criticalChangeDepth | 
                            state.getDepth() - state.getDepth() > criticalChangeDepth) {
                        am.setAlertType(0);
                        am.setSensor(0);
                        AlertMessageQueue.add(am);
                    }
                }
                
                /** Generates an alert of type CriticalMax in Water Depth */
                if (fields.getDepth() > criticalMaxDepth) {
                    am.setAlertType(1);
                    am.setSensor(0);
                    AlertMessageQueue.add(am);
                }
                
                /** Generates an alert of type CriticalMin in Water Depth */
                if (fields.getDepth() < criticalMinDepth) {
                    am.setAlertType(2);
                    am.setSensor(0);
                    AlertMessageQueue.add(am);
                }
                
                /** Updates current values of Water Depth and Offset */   
                state.setDepth(fields.getDepth());
                state.setOffset(fields.getOffset());
                
                first_d = false;
                
                break;
            
            /** 
             * If a packet is of type WindData, png = 130306, 
             * generates Alert Messages and puts them on AlertMessageQueue, of types: 
             * no data (timeout), criticalChange, criticalMax or criticalMin values
             * updates the current value of Speed and Angle
             */   
            case 130306:
                
                /** If it is the first message of this type, NO criticalChange alert */
                if (!first_w) {
                    
                    /** Generates an alert of type CriticalChange in Wind Speed */
                    if (fields.getWindSpeed() - state.getWindSpeed() > criticalChangeWindSpeed | 
                            state.getWindSpeed() - fields.getWindSpeed() > criticalChangeWindSpeed) {
                        am.setAlertType(0);
                        am.setSensor(1);
                        AlertMessageQueue.add(am);
                    }
                    
                    /** Generates an alert of type CriticalChange in Wind Angle */
                    if (fields.getWindAngle() - state.getWindAngle() > criticalChangeWindAngle | 
                            state.getWindAngle() - fields.getWindAngle() > criticalChangeWindAngle) {
                        am.setAlertType(0);
                        am.setSensor(2);
                        AlertMessageQueue.add(am);
                    }    
                }
                
                /** Generates an alert of type CriticalMax in WindSpeed */
                if (fields.getWindSpeed() > criticalMaxWindSpeed) {
                    am.setAlertType(1);
                    am.setSensor(1);
                    AlertMessageQueue.add(am);
                }
                
                /** Generates an alert of type CriticalMin in WindSpeed */
                if (fields.getWindSpeed() < criticalMinWindSpeed) {
                    am.setAlertType(2);
                    am.setSensor(1);
                    AlertMessageQueue.add(am);
                }
                
                /** Updates current values of Wind Speed and Wind Angle */               
                state.setWindSpeed(fields.getWindSpeed());
                state.setWindAngle(fields.getWindAngle());
                
                first_w = false;
                
                break;
            
            /** 
             * If a packet is of type BoatHeading, png = 127250, 
             * generates Alert Messages and puts them on AlertMessageQueue, of types: 
             * no data (timeout), criticalChange
             * updates the current value of Speed and Angle
             */        
            case 127250:
                
                /** If it is the first message of this type, NO criticalChange alert */
                if (!first_h) {
                    
                    /** Generates an alert of type CriticalChange in Heading */
                    if (fields.getHeading() - state.getHeading() > criticalChangeHeading | 
                    state.getHeading() - fields.getHeading() > criticalChangeHeading) {
                        am.setAlertType(0);
                        am.setSensor(3);
                        AlertMessageQueue.add(am);
                    }
                }
                
                /** Updates current values of Heading, Deviation, and Variation */         
                state.setHeading(fields.getHeading());
                state.setDeviation(fields.getDeviation());
                state.setVariation(fields.getVariation());
                
                first_h = false;
                
                break;
                
            /** 
             * If a packet is of type BoatSpeed, png = 128259, 
             * generates Alert Messages and puts them on AlertMessageQueue, of types: 
             * no data (timeout), criticalChange, criticalMax or criticalMin values
             * updates the current value of Speed and Angle
             */   
            case 128259:
                
                /** If it is the first message of this type, NO criticalChange alert */
                if (!first_s) {
                    
                    /** Generates an alert of type CriticalChange in BoatSpeed */
                    if (fields.getSpeedWaterReferenced() - state.getSpeedWaterReferenced() > criticalChangeSpeed | 
                            state.getSpeedWaterReferenced() - fields.getSpeedWaterReferenced() > criticalChangeSpeed) {
                        am.setAlertType(0);
                        am.setSensor(4);
                        AlertMessageQueue.add(am);
                    }
                }
                
                /** Generates an alert of type CriticalMax in BoatSpeed */
                if (fields.getSpeedWaterReferenced() > criticalMaxBoatSpeed) {
                    am.setAlertType(1);
                    am.setSensor(4);
                    AlertMessageQueue.add(am);
                }
                
                /** Generates an alert of type CriticalMin in BoatSpeed */
                if (fields.getSpeedWaterReferenced() < criticalMinBoatSpeed) {
                    am.setAlertType(2);
                    am.setSensor(4);
                    AlertMessageQueue.add(am);
                }
                
                /** Updates current values of BoatSpeed */ 
                state.setSpeedWaterReferenced(fields.getSpeedWaterReferenced());
                
                first_s = false;
                
                break;
                
            default:
                    // "CANNOT DECODE A MESSAGE!"
                    break;
            }
         
        }
        
    }
    

}
package uk.ac.cam.november.decoder;

import java.util.Queue;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;

import uk.ac.cam.november.packet.Fields;
import uk.ac.cam.november.packet.Packet;

/**
 * This class initializes the current Boat State, composed of values of: -->
 * From Water Depth Sensor: Water Depth and WaterDepthOffset; --> From Wind
 * Sensor: WindSpeed and WindAngle; --> From Boat Speed Sensor:
 * BoatSpeedReferenced; --> From Boat Heading Sensor: BoatHeading,
 * BoatHeadingDerivation, BoatHeadingVariation.
 * 
 * Receives decoded NMEA packets and updates the current Boat State
 * 
 * Generates 4 types of alerts and puts them onto AlertMessageQueue: --> type0:
 * the state of the boat has critically changed: CriticalChange alert; -->
 * type1: a value of the state of the boat is critically large: CriticalMax
 * value alert; --> type2: a value of the state of the boat is critically small:
 * CriticalMin value alert; --> type4: the module stopped receiving packets for
 * more than 10s (stale data): TimeOut alert.
 * 
 * @author Marie Menshova
 *
 */

public class MessageDecoder implements Runnable {

    /** Values describing critical state of the system */
    int criticalMinDepth = 20;
    int criticalChangeDepth = 40;
    int criticalMaxWindSpeed = 75;
    int criticalChangeWindSpeed = 40;
    int criticalChangeWindAngle = 40;
    int criticalChangeHeading = 40;
    int criticalMinBoatSpeed = 4;
    int criticalMaxBoatSpeed = 20;
    int criticalChangeSpeed = 10;

    long lastTimeD;
    long lastTimeWS;
    long lastTimeWA;
    long lastTimeH;
    long lastTimeS;
    long curTime;
    long lastTimeDAlert;
    long lastTimeWSAlert;
    long lastTimeWAAlert;
    long lastTimeHAlert;
    long lastTimeSAlert;

    long timeOutTime = 10000L; // 10s
    long timeOutTimeAlert = 5000L;  // 5s

    private Queue<Packet> MessageQueue;
    private Queue<AlertMessage> AlertMessageQueue;

    BoatState state = new BoatState();

    public MessageDecoder(Queue<Packet> messageQueue) {
        this.MessageQueue = messageQueue;
        AlertMessageQueue = Queues.synchronizedQueue(EvictingQueue.create(30));
    }

    public Queue<AlertMessage> getAlertMessageQueue() {
        return AlertMessageQueue;
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

        state.setLatitude(0);
        state.setLongtitude(0);
        state.setAltitude(0);

        boolean first_d = true;
        boolean first_w = true;
        boolean first_h = true;
        boolean first_s = true;

        lastTimeD = System.currentTimeMillis();
        lastTimeWS = System.currentTimeMillis();
        lastTimeWA = System.currentTimeMillis();
        lastTimeH = System.currentTimeMillis();
        lastTimeS = System.currentTimeMillis();

        while (true) {
            /** Receive a packet from NMEA input */
            Packet packet = MessageQueue.poll();

            if (packet != null) {

                int packetID = packet.getPgn();
                Fields fields = (Fields) packet.getFields();

                switch (packetID) {

                /**
                 * If a packet is of type WaterDepth, png = 128267, generates
                 * Alert Messages and puts them on AlertMessageQueue, of types:
                 * no data (timeout), criticalChange, criticalMax or criticalMin
                 * values updates the current value of Speed and Angle
                 */
                case 128267:

                    /**
                     * Initializes the last time since WaterDepth packet was
                     * received
                     */
                    lastTimeD = System.currentTimeMillis();
                    
                    if (lastTimeD - lastTimeDAlert > timeOutTimeAlert) {
                        
                        /**
                         * If it is the first message of this type, NO
                         * criticalChange alert
                         */
                     
                        if (!first_d) {
    
                            /**
                             * Generates an alert of type CriticalChange in Water
                             * Depth
                             */
                            
                            if (fields.getDepth() - state.getDepth() > criticalChangeDepth
                                    | state.getDepth() - state.getDepth() > criticalChangeDepth) {
                                AlertMessage amD0 = new AlertMessage();
                                amD0.setAlertType(0);
                                amD0.setSensor(0);
                                AlertMessageQueue.add(amD0);
                            }
                        }
    
                        /** Generates an alert of type CriticalMin in Water Depth */
                        if (fields.getDepth() < criticalMinDepth) {
                            AlertMessage amD2 = new AlertMessage();
                            amD2.setAlertType(2);
                            amD2.setSensor(0);
                            AlertMessageQueue.add(amD2);
                        }
                    
                        lastTimeDAlert = System.currentTimeMillis();
                    }

                    /** Updates current values of Water Depth and Offset */
                    state.setDepth(fields.getDepth());
                    state.setOffset(fields.getOffset());
                    
                    

                    first_d = false;

                    break;

                /**
                 * If a packet is of type WindData, png = 130306, generates
                 * Alert Messages and puts them on AlertMessageQueue, of types:
                 * no data (timeout), criticalChange, criticalMax or criticalMin
                 * values updates the current value of Speed and Angle
                 */
                case 130306:

                    /**
                     * Initializes the last time since WindData packet was
                     * received
                     */
                    lastTimeWS = System.currentTimeMillis();
                    lastTimeWA = System.currentTimeMillis();
                    
                    if (lastTimeWA - lastTimeWSAlert > timeOutTimeAlert |
                            lastTimeWS - lastTimeWAAlert > timeOutTimeAlert) {

                        /**
                         * If it is the first message of this type, NO
                         * criticalChange alert
                         */
                        if (!first_w) {
    
                            /**
                             * Generates an alert of type CriticalChange in WindSpeed
                             * Speed
                             */
                            if (fields.getWindSpeed() - state.getWindSpeed() > criticalChangeWindSpeed
                                    | state.getWindSpeed() - fields.getWindSpeed() > criticalChangeWindSpeed) {
                                AlertMessage amWS0 = new AlertMessage();
                                amWS0.setAlertType(0);
                                amWS0.setSensor(1);
                                AlertMessageQueue.add(amWS0);
                            }
    
                            /**
                             * Generates an alert of type CriticalChange in WindAngle
                             * Angle
                             */
                            if (fields.getWindAngle() - state.getWindAngle() > criticalChangeWindAngle
                                    | state.getWindAngle() - fields.getWindAngle() > criticalChangeWindAngle) {
                                AlertMessage amWA0 = new AlertMessage();
                                amWA0.setAlertType(0);
                                amWA0.setSensor(2);
                                AlertMessageQueue.add(amWA0);
                            }
                        }
    
                        /** Generates an alert of type CriticalMax in WindSpeed */
                        if (fields.getWindSpeed() > criticalMaxWindSpeed) {
                            AlertMessage amWS1 = new AlertMessage();
                            amWS1.setAlertType(1);
                            amWS1.setSensor(1);
                            AlertMessageQueue.add(amWS1);
                        }
                        
                        lastTimeWSAlert = System.currentTimeMillis();
                        lastTimeWAAlert = System.currentTimeMillis();
                    
                    }

                    /** Updates current values of Wind Speed and Wind Angle */
                    state.setWindSpeed(fields.getWindSpeed());
                    state.setWindAngle(fields.getWindAngle());

                    first_w = false;

                    break;

                /**
                 * If a packet is of type BoatHeading, png = 127250, generates
                 * Alert Messages and puts them on AlertMessageQueue, of types:
                 * no data (timeout), criticalChange updates the current value
                 * of Speed and Angle
                 */
                case 127250:

                    /**
                     * Initializes the last time since BoatHeading packet was
                     * received
                     */
                    lastTimeH = System.currentTimeMillis();
                    
                    if (lastTimeH - lastTimeHAlert > timeOutTimeAlert) {

                        /**
                         * If it is the first message of this type, NO
                         * criticalChange alert
                         */
                        if (!first_h) {
    
                            /**
                             * Generates an alert of type CriticalChange in Heading
                             */
                            if (fields.getHeading() - state.getHeading() > criticalChangeHeading
                                    | state.getHeading() - fields.getHeading() > criticalChangeHeading) {
                                AlertMessage amH0 = new AlertMessage();
                                amH0.setAlertType(0);
                                amH0.setSensor(3);
                                AlertMessageQueue.add(amH0);
                            }
                        }
                        
                        lastTimeHAlert = System.currentTimeMillis();
                    }

                    /**
                     * Updates current values of Heading, Deviation, and
                     * Variation
                     */
                    state.setHeading(fields.getHeading());
                    state.setDeviation(fields.getDeviation());
                    state.setVariation(fields.getVariation());

                    first_h = false;

                    break;

                /**
                 * If a packet is of type BoatSpeed, png = 128259, generates
                 * Alert Messages and puts them on AlertMessageQueue, of types:
                 * no data (timeout), criticalChange, criticalMax or criticalMin
                 * values updates the current value of Speed and Angle
                 */
                case 128259:

                    /**
                     * Initializes the last time since BoatSpeed packet was
                     * received
                     */
                    lastTimeS = System.currentTimeMillis();
                    
                    if (lastTimeS - lastTimeSAlert > timeOutTimeAlert) {

                        /**
                         * If it is the first message of this type, NO
                         * criticalChange alert
                         */
                        if (!first_s) {
    
                            /**
                             * Generates an alert of type CriticalChange in
                             * BoatSpeed
                             */
                            if (fields.getSpeedWaterReferenced() - state.getSpeedWaterReferenced() > criticalChangeSpeed
                                    | state.getSpeedWaterReferenced()
                                            - fields.getSpeedWaterReferenced() > criticalChangeSpeed) {
                                AlertMessage amB0 = new AlertMessage();
                                amB0.setAlertType(0);
                                amB0.setSensor(4);
                                AlertMessageQueue.add(amB0);
                            }
                            
                            lastTimeSAlert = System.currentTimeMillis();
                        }
    
                        /** Generates an alert of type CriticalMax in BoatSpeed */
                        if (fields.getSpeedWaterReferenced() > criticalMaxBoatSpeed) {
                            AlertMessage amB1 = new AlertMessage();
                            amB1.setAlertType(1);
                            amB1.setSensor(4);
                            AlertMessageQueue.add(amB1);
                        }
                    
                    }

                    /** Updates current values of BoatSpeed */
                    state.setSpeedWaterReferenced(fields.getSpeedWaterReferenced());

                    first_s = false;

                    break;

                // Dealing with GPS coordinates
                case 129029:
                    // GPS coordinates will not generate alerts
                    state.setLatitude(fields.getLatitude());
                    state.setLongtitude(fields.getLongtitude());
                    state.setAltitude(fields.getAltitude());
                    break;

                default:
                    // "CANNOT DECODE A MESSAGE!"
                    break;
                }

            }

            curTime = System.currentTimeMillis();

            /**
             * Generates an alert of type TimeOut of WaterDepth if the data has
             * been stale for 10s
             */
            long diffD = curTime - lastTimeD;
            if (diffD > timeOutTime) {
                AlertMessage amD3 = new AlertMessage();
                amD3.setAlertType(3);
                amD3.setSensor(0);
                AlertMessageQueue.add(amD3);
                lastTimeD = System.currentTimeMillis();
            }

            /**
             * Generates an alert of type TimeOut of WindSpeed if the data has
             * been stale for 10s
             */
            long diffWS = curTime - lastTimeWS;
            if (diffWS > timeOutTime) {
                AlertMessage amWS3 = new AlertMessage();
                amWS3.setAlertType(3);
                amWS3.setSensor(1);
                AlertMessageQueue.add(amWS3);
                lastTimeWS = System.currentTimeMillis();
            }

            /**
             * Generates an alert of type TimeOut of WindAngle if the data has
             * been stale for 10s
             */
            long diffWA = curTime - lastTimeWA;
            if (diffWA > timeOutTime) {
                AlertMessage amWA3 = new AlertMessage();
                amWA3.setAlertType(3);
                amWA3.setSensor(2);
                AlertMessageQueue.add(amWA3);
                lastTimeWA = System.currentTimeMillis();
            }

            /**
             * Generates an alert of type TimeOut of BoatHeading if the data has
             * been stale for 10s
             */
            long diffH = curTime - lastTimeH;
            if (diffH > timeOutTime) {
                AlertMessage amH3 = new AlertMessage();
                amH3.setAlertType(3);
                amH3.setSensor(3);
                AlertMessageQueue.add(amH3);
                lastTimeH = System.currentTimeMillis();
            }

            /**
             * Generates an alert of type TimeOut of BoatSpeed if the data has
             * been stale for 10s
             */
            long diffS = curTime - lastTimeS;
            if (diffS > timeOutTime) {
                AlertMessage amB3 = new AlertMessage();
                amB3.setAlertType(3);
                amB3.setSensor(4);
                AlertMessageQueue.add(amB3);
                lastTimeS = System.currentTimeMillis();
            }
        }

    }

}

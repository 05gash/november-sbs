package uk.ac.cam.november.decoder;

import java.util.Queue;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;

import uk.ac.cam.november.buttons.ButtonNames;
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
    int criticalMaxBoatSpeed = 25;
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

    private static final float INFINITY = 1000000.0f;    

    /* Type of Sensor that provoked an AlertMessage
       0 - WaterDepth, 1 - WindSpeed, 2 - WindAngle, 3 - BoatHeading, 4 - BoatSpeed
       DataStatus(final String dataNameInput, final int sensorInput, final float maxChangeLimitInput,
                  final float minDataValueInput, final float maxDataValueInput)
    */
    private final DataStatus waterDepthState = new DataStatus(ButtonNames.WATER_DEPTH, 0, 20.0f, 40.0f, INFINITY);
    private final DataStatus windSpeedState = new DataStatus(ButtonNames.WIND_SPEED, 1, 40.0f, -INFINITY, 75.0f);
    // Changed critical wind angle change from 40 to 50, to avoid collision with critical boat change.
    // (Because when the boat turns, the wind angle also automatically turns)
    private final DataStatus windAngleState = new DataStatus(ButtonNames.WIND_DIRECTION, 2, 100.0f, -INFINITY, INFINITY);
    private final DataStatus boatHeadingState = new DataStatus(ButtonNames.COMPASS_HEADING, 3, 40.0f, -INFINITY, INFINITY);                  
    private final DataStatus boatSpeedState = new DataStatus(ButtonNames.BOAT_SPEED, 4, 3.5f, -INFINITY, 22.0f);

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


    public void addAlertsToQueue(final AlertMessage[] alertMessages) {
        for (int i = 0; i < alertMessages.length; i++) {
            if (alertMessages[i] != null) {
                AlertMessageQueue.add(alertMessages[i]);
            }
        }
    }

    private void checkForTimeouts() {
        DataStatus[] dataStatusArray = {waterDepthState, windSpeedState, windAngleState, boatHeadingState, boatSpeedState};
        for (final DataStatus dataStatus : dataStatusArray) {
            final AlertMessage timeoutAlert = dataStatus.generateTimeoutMessage();
            if (timeoutAlert != null) {
                AlertMessageQueue.add(timeoutAlert);
            }
        }
    }

    @Override
    public void run() {

        while (true) {
            checkForTimeouts();
 
            /** Receive a packet from NMEA input */
            Packet packet = MessageQueue.poll();

            if (packet != null) {

                int packetID = packet.getPgn();
                Fields fields = (Fields) packet.getFields();

                AlertMessage[] alertMessages;
                switch (packetID) {

                    case 128267: // Water depth
                        waterDepthState.generateAlerts(AlertMessageQueue, state.getDepth(), fields.getDepth());
                        state.setDepth(fields.getDepth());
                        state.setOffset(fields.getOffset());
                        
                        break;

                    case 130306: // Wind data
                        windSpeedState.generateAlerts(AlertMessageQueue, state.getWindSpeed(), fields.getWindSpeed());
                        state.setWindSpeed(fields.getWindSpeed());
                        
                        windAngleState.generateAlerts(AlertMessageQueue, state.getWindAngle(), fields.getWindAngle());
                        state.setWindAngle(fields.getWindAngle());

                        break;

                    case 127250: // Boad heading
                        boatHeadingState.generateAlerts(AlertMessageQueue, state.getHeading(), fields.getHeading());
                        state.setHeading(fields.getHeading());
                        state.setDeviation(fields.getDeviation());
                        state.setVariation(fields.getVariation());

                        break;

                    case 128259: // Boad speed
                        boatSpeedState.generateAlerts(AlertMessageQueue, state.getSpeedWaterReferenced(), fields.getSpeedWaterReferenced());
                        state.setSpeedWaterReferenced(fields.getSpeedWaterReferenced());

                        break;

                    case 129029: // GPS coordinates
                        // GPS coordinates will not generate alerts
                        state.setLatitude(fields.getLatitude());
                        state.setLongtitude(fields.getLongtitude());
                        state.setAltitude(fields.getAltitude());
                    
                        break;

                    default:
                        // "CANNOT DECODE A MESSAGE!"
                        break;
                }

            }else{
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }
}


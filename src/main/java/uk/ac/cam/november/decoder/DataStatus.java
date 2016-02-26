package uk.ac.cam.november.decoder;

import java.lang.Math;
import java.util.Queue;

import uk.ac.cam.november.buttons.ButtonNames;

/**
 * This is handles any piece of data that the system wishes to output.
 * We use ONE instance per ONE data type because some devices record more
 * information than the others, e.g. Wind Sensor, which generates Wind Angle
 * and Wind Speed. So, there will be two instances generated for Wind Data.
 *
 * @author
 */

class DataStatus {

    /** If no more packets were received for this time, generate an alert*/
    private static final Long TIME_OUT_ALERT = 60000L;  // 1 minute

    /** Initializes a time interval between generating alerts or types MaxValue, MinValue.
     * The first alert is generated immediately, and subsequent messages will use
     * this timestamp */
    private static final Long DATA_OUT_OF_RANGE_TIMESTAMP = 15000L;  // 10 seconds

    /** Initializes a time interval between generating alerts or types CriticalChange.
     * The first alert is generated immediately, and subsequent messages will use
     * this timestamp */
    private static final Long RAPID_DATA_CHANGE_TIMESTAMP = 4000L; // 3 seconds

    private final String dataName;
    // Sensor type is just used to make it compatible with AlertMessage class.
    // TODO(ml693): this is still stupid implementation. String dataName uniquely
    // identifies sensor and is more readable than int sensor.
    // If we have more time, refactor the AlertMessage class as well.
    private final int sensor;

    /** These are thresholds for generating alerts of types: 
     * CriticalChange, MinValue, and MaxValue */
    private final float maxChangeLimit;
    private final float minDataValue;
    private final float maxDataValue; 


    /** Timestamps for the last time since last alert of a certain type */
    private Long lastTimeoutAlert;
    private Long lastRapidChangeAlert;
    private Long lastOutOfRangeAlert;

    DataStatus(final String dataNameInput, final int sensorInput, final float maxChangeLimitInput,
               final float minDataValueInput, final float maxDataValueInput) {
        dataName = dataNameInput;
        sensor = sensorInput;
        maxChangeLimit = maxChangeLimitInput;
        minDataValue = minDataValueInput;
        maxDataValue = maxDataValueInput;
       
        final Long currentTime = System.currentTimeMillis();
        lastTimeoutAlert = currentTime;
        lastRapidChangeAlert = currentTime;
        lastOutOfRangeAlert = currentTime;
    }

    // returns null if alert message does not
    // need to be generated
    AlertMessage generateTimeoutMessage() {
        // Case when alert message has been recently generated,
        // or when the device is working
        final Long currentTime = System.currentTimeMillis();
        if (currentTime - lastTimeoutAlert < TIME_OUT_ALERT) {
            return null;
        }
       
        // Case when data was not received for a long time 
        lastTimeoutAlert = currentTime;
        return (new AlertMessage(3, sensor));
    }


    // returns null if alert message does not
    // need to be generated or if it has
    // just been generated.
    AlertMessage generateCriticalChangeMessage(final float lastData, final float newData) {
        final Long currentTime = System.currentTimeMillis();
        if (Math.abs(newData - lastData) <= maxChangeLimit || 
            currentTime - lastRapidChangeAlert < RAPID_DATA_CHANGE_TIMESTAMP ) {
            return null;
        }

        // Handling special cases of boat angle and wind angle:
        // Special handling is needed because transitioning
        // from 0 degrees to 359 degrees is not a rapid
        // change of data, but looks as a big change in numbers
        if ((dataName.compareTo(ButtonNames.WIND_DIRECTION) == 0) ||
            (dataName.compareTo(ButtonNames.COMPASS_HEADING) == 0)) {
            // Assumption is made that if a direction is rapid,
            // it will not change more than 180 degrees
            if (Math.abs(newData - lastData) >= 180.f) {
                return null;
            } 
        } 

        lastRapidChangeAlert = currentTime;
        return (new AlertMessage(0, sensor));
    }
    
    // returns null if alert message does not
    // need to be generated or if it has
    // just been generated.
    AlertMessage generateOutOfRangeAlert(final float currentData) {
        if (currentData >= minDataValue && currentData <= maxDataValue) {
            return null;
        }

        final Long currentTime = System.currentTimeMillis();
        if (currentTime - lastOutOfRangeAlert < DATA_OUT_OF_RANGE_TIMESTAMP) {
            return null;
        }

        lastOutOfRangeAlert = currentTime;
        final int dataTooLowOrHigh = (currentData < minDataValue) ? 2 : 1;
        return (new AlertMessage(dataTooLowOrHigh, sensor));
    }

    // The first time a packet is inspected,
    // we will not generate alerts,
    // only save the state.
    private boolean synchronizedData = false;
    void generateAlerts(final Queue<AlertMessage> alertQueue, final float lastData, final float newData) {
        lastTimeoutAlert = System.currentTimeMillis();

        final AlertMessage[] alertMessages = new AlertMessage[2];
        if (synchronizedData) {      
            alertMessages[0] = generateCriticalChangeMessage(lastData, newData);
            alertMessages[1] = generateOutOfRangeAlert(newData);
        }

        for (final AlertMessage alertMessage : alertMessages) {
            if (alertMessage != null) {
                alertQueue.add(alertMessage);
            }
        }

        synchronizedData = true;
    } 

}

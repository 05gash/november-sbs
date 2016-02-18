package uk.ac.cam.november.buttons;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

class GpioListenerTest {

    public static void main(final String[] args) {

        final Thread listener = new Thread() {
            public void run() {
                final GpioController gpio = GpioFactory.getInstance();

                // 29 is last GPIO general purpose pin, which will not be used
                // by other libraries.
                final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29,
                        PinPullResistance.PULL_DOWN);

                // create and register gpio pin listener
                button.addListener(new GpioPinListenerDigital() {
                    @Override
                    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                        // TODO(ml693): write appropriate handle, which would
                        // aid in testing
                    }

                });

                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        };
        listener.run();

    }
}

package uk.ac.cam.november.simulation;

import java.io.IOException;

import uk.ac.cam.november.packet.Packet;
import uk.ac.cam.november.simulation.network.SimulatorClient;
import uk.ac.cam.november.simulation.ui.SimulatorUI;

/**
 * This class represents the entry point to the boat simulator which can be used
 * for testing and demonstration of the system.
 * 
 * @author Jamie Wood
 *
 */
public class Simulator {

    private SimulatorUI ui;
    private WorldModel worldModel;
    private BoatDataOutputter dataOutput;

    private SimulatorClient netClient;

    private Thread runThread;

    /**
     * Constructs the simulator. Creates the
     * {@link uk.ac.cam.november.simulation.ui.SimulatorUI SimulatorUI} user
     * interface but does not show it.
     */
    public Simulator(String serverAddress, final float initialLatitude, final float initialLongtitude) {
        worldModel = new WorldModel(initialLatitude, initialLongtitude);
        dataOutput = new BoatDataOutputter(this);
        ui = new SimulatorUI(this);

        try {
            netClient = new SimulatorClient(ui, serverAddress);
        } catch (IOException e) {
            System.err.println("Failed to open connection to simulator server");
            System.err.println("ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to connecto to the server.");
        }

        runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long oTime = System.currentTimeMillis();
                while (true) {
                    long nTime = System.currentTimeMillis();
                    step((nTime - oTime) / 1000f);

                    oTime = nTime;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Simulator-UI");
    }

    /**
     * Shows the user interface for the simulator.
     */
    public void showUI() {
        ui.setVisible(true);
    }

    /**
     * Step the world model by {@code dt} seconds. Also causes the display to be
     * redrawn.
     * 
     * @param dt
     *            Number of seconds to advance time by
     */

    
    private static final float HEADING_ACCELERATION = 0.015f;
    private static final float SPEED_ACCELERATION = 0.0007f;
    private static final float SPEED_DECELARATION = 0.10f;
    private static final float NO_CHANGE = 0.0f;
    // if none of the left / right buttons are clicked,
    // we will slow down the heading quickly based
    // on SLOW_DOWN_RATE.
    private static final float SLOW_DOWN_RATE = 0.7f; // should be less than 1.0
    
    private float headingChange = NO_CHANGE;
    private float speedChangeForwards = NO_CHANGE;

    public void step(float dt) {        
        // Dealing with how quickly the boat 
        // should move forwards
        if (ui.KEY_UP) {
            worldModel.setBoatSpeed(worldModel.getBoatSpeed() + speedChangeForwards);
            speedChangeForwards += SPEED_ACCELERATION;
        } else {
            speedChangeForwards = NO_CHANGE;
        }
        if (ui.KEY_DOWN) {
            worldModel.setBoatSpeed(worldModel.getBoatSpeed() - SPEED_DECELARATION);
            speedChangeForwards = NO_CHANGE;
        }

        // Dealing with how quickly the boat
        // should rotate left / right
        if (ui.KEY_LEFT && !ui.KEY_RIGHT) {
            headingChange -= HEADING_ACCELERATION;
        }
        if (!ui.KEY_LEFT && ui.KEY_RIGHT) {
            headingChange += HEADING_ACCELERATION;
        }
        if (!ui.KEY_LEFT && !ui.KEY_RIGHT) {
            final float slowDown = HEADING_ACCELERATION * SLOW_DOWN_RATE;
            if (headingChange > slowDown) {
                headingChange -= slowDown;
            }
            if (headingChange < -slowDown) {
                headingChange += slowDown;
            }
            if (headingChange >= -slowDown && headingChange <= slowDown) {
                headingChange = NO_CHANGE;
            }
        }
        worldModel.setHeading(worldModel.getHeading() + headingChange);
         
        worldModel.step(dt);
        try{
            dataOutput.update();
        }catch(IOException e){
            System.err.println("Failed to write data packet to socket");
            System.err.println("ERROR: " + e.getMessage());
            netClient.close();
            throw new RuntimeException("Failed to write data to port");
        }
        ui.revalidate();
        ui.repaint();
    }

    /**
     * Gets the instance of the world model in use.
     * 
     * @return WorldModel instance which stores the simulator state.
     */
    public WorldModel getWorldModel() {
        return worldModel;
    }

    /**
     * Adds a message to the output queue.
     * 
     * @param p
     *            The message to add.
     */
    public void queueMessage(Packet p) throws IOException {
        System.out.println("Sending a " + p.getDescription() + " packet at " + p.getTimestamp());
        netClient.sendPacket(p);
    }

    /**
     * Returns the thread used to run the simulation.
     * 
     * @return Thread The thread which should be run by the invoking process.
     */
    public Thread getThread() {
        return runThread;
    }

}

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
     * @throws IOException 
     */
    public Simulator(String serverAddress) throws IOException {
        worldModel = new WorldModel();
        dataOutput = new BoatDataOutputter(this);
        ui = new SimulatorUI(this);
        netClient = new SimulatorClient(serverAddress);
        

        runThread = new Thread(new Runnable() {
            @Override
            public void run() {
                long oTime = System.currentTimeMillis();
                while (true) {
                    try {
                        long nTime = System.currentTimeMillis();
                        step((nTime - oTime) / 1000f);

                        oTime = nTime;
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
     * @throws IOException 
     */
    public void step(float dt) throws IOException {

        if (ui.KEY_UP) {
            worldModel.setBoatSpeed(worldModel.getBoatSpeed() + 0.5f);
        }
        if (ui.KEY_DOWN) {
            worldModel.setBoatSpeed(worldModel.getBoatSpeed() - 0.5f);
        }
        if (ui.KEY_LEFT) {
            worldModel.setHeading(worldModel.getHeading() - 1.5f);
        }
        if (ui.KEY_RIGHT) {
            worldModel.setHeading(worldModel.getHeading() + 1.5f);
        }

        worldModel.step(dt);
        try{
            dataOutput.update();
        }catch(IOException e){
            throw new IOException("Simulator Failed to write packet to socket");
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

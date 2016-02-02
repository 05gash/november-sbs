package uk.ac.cam.november.simulation;

import uk.ac.cam.november.message.Message;
import uk.ac.cam.november.simulation.ui.SimulatorUI;

/**
 * This class represents the entry point to the boat simulator which can be used
 * for testing and demonstration of the system.`
 * 
 * @author Jamie Wood
 *
 */
public class Simulator {

    private SimulatorUI ui;
    private WorldModel worldModel;

    /**
     * Constructs the simulator. Creates the
     * {@link uk.ac.cam.november.simulation.ui.SimulatorUI SimulatorUI} user
     * interface but does not show it.
     */
    public Simulator() {
        worldModel = new WorldModel();
        ui = new SimulatorUI(this);
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
    public void step(float dt) {
        worldModel.step(dt);
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
     * @param m
     *            The message to add.
     */
    public void queueMessage(Message m) {
        System.out.println("Queueing a " + m.getDescription() + " packet at " + m.getTimestamp());
    }

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.showUI();
        long oTime = System.currentTimeMillis();
        while (true) {
            long nTime = System.currentTimeMillis();
            sim.step((nTime - oTime) / 1000f);

            oTime = nTime;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package uk.ac.cam.november.simulation;

import uk.ac.cam.november.simulation.ui.SimulatorUI;

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

    public void step(float dt){
        worldModel.step(dt);
        ui.repaint();
    }
    
    public WorldModel getWorldModel() {
        return worldModel;
    }

    public static void main(String[] args) {
        Simulator sim = new Simulator();
        sim.showUI();
        while(true){
            sim.step(0.1f);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

package uk.ac.cam.november.simulation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.HeadlessException;
import java.lang.reflect.Field;
import java.net.ServerSocket;

import org.junit.Test;

import uk.ac.cam.november.simulation.network.SimulatorServer;
import uk.ac.cam.november.simulation.ui.SimulatorUI;

public class SimulatorTest {
    
    @Test
    public void simulatorUIDefaultsToHidden() throws Exception {
        SimulatorServer ss = new SimulatorServer();
        Simulator sim = new Simulator(null, 0, 0);
        SimulatorUI simUI = getSimulatorUI(sim);
        assertFalse(simUI.isVisible());
        Field socketField = SimulatorServer.class.getDeclaredField("listenSocket");
        socketField.setAccessible(true);
        ((ServerSocket)socketField.get(ss)).close();
    }
    
    @Test
    public void simulatorUIIsShown() throws Exception {
        try{
            SimulatorServer ss = new SimulatorServer();
            Simulator sim = new Simulator(null, 0, 0);
            sim.showUI();
            SimulatorUI simUI = getSimulatorUI(sim);
            assertTrue(simUI.isVisible());
            Field socketField = SimulatorServer.class.getDeclaredField("listenSocket");
            socketField.setAccessible(true);
            ((ServerSocket)socketField.get(ss)).close();
        }catch(HeadlessException he){
        }
    }
    
    private SimulatorUI getSimulatorUI(Simulator sim) throws Exception {
        Field uiField = Simulator.class.getDeclaredField("ui");
        uiField.setAccessible(true);
        return (SimulatorUI)uiField.get(sim);
    }
    
}

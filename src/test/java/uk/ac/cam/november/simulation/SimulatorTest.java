package uk.ac.cam.november.simulation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Test;

import uk.ac.cam.november.simulation.ui.SimulatorUI;

public class SimulatorTest {
    
    @Test
    public void simulatorUIDefaultsToHidden() throws Exception {
        Simulator sim = new Simulator();
        SimulatorUI simUI = getSimulatorUI(sim);
        assertFalse(simUI.isVisible());
    }
    
    @Test
    public void simulatorUIIsShown() throws Exception {
        Simulator sim = new Simulator();
        sim.showUI();
        SimulatorUI simUI = getSimulatorUI(sim);
        assertTrue(simUI.isVisible());
    }
    
    private SimulatorUI getSimulatorUI(Simulator sim) throws Exception {
        Field uiField = Simulator.class.getDeclaredField("ui");
        uiField.setAccessible(true);
        return (SimulatorUI)uiField.get(sim);
    }
    
}

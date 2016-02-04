package uk.ac.cam.november.simulation.ui;

import javax.swing.Box;
import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class InstrumentPanel extends JPanel {
    private static final long serialVersionUID = -8873363538918460588L;
    
    public InstrumentPanel(WorldModel worldModel){
        Box box = Box.createVerticalBox();
        
        CompassPanel compassPanel = new CompassPanel(worldModel);
        WindPanel windPanel = new WindPanel(worldModel);
        DepthPanel depthPanel = new DepthPanel(worldModel);
        SpeedPanel speedPanel = new SpeedPanel(worldModel);
        
        box.add(compassPanel);
        box.add(windPanel);
        box.add(depthPanel);
        box.add(speedPanel);
        
        add(box);
    }
    
}

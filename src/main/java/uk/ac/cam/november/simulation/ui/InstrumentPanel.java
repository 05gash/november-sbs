package uk.ac.cam.november.simulation.ui;

import javax.swing.Box;
import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class InstrumentPanel extends JPanel {
    private static final long serialVersionUID = -8873363538918460588L;
    
    private WorldModel worldModel;
    
    public InstrumentPanel(WorldModel worldModel){
        this.worldModel = worldModel;
        
        Box box = Box.createVerticalBox();
        
        CompassPanel compassPanel = new CompassPanel(worldModel);
        
        box.add(compassPanel);
        
        add(box);
    }
    
}

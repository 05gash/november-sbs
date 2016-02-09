package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.StepListener;
import uk.ac.cam.november.simulation.WorldModel;

public class DepthPanel extends JPanel implements StepListener {
    private static final long serialVersionUID = 2494838642150810963L;

    private WorldModel worldModel;
    private GraphPanel graphPanel;

    public DepthPanel(WorldModel worldModel) {
        this.worldModel = worldModel;
        setPreferredSize(new Dimension(250, 100));

        graphPanel = new GraphPanel(250, GraphPanel.FILLED);

        add(graphPanel);

        worldModel.registerStepListener(this);
    }

    @Override
    public void onStep() {
        graphPanel.addValue(worldModel.getWaterDepth());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D gr = (Graphics2D) g;

        gr.setColor(Color.BLACK);
        String angleMessage = "Water Depth: " + String.format("%.2f", worldModel.getWaterDepth()) + "m";
        gr.drawString(angleMessage, 8, 16);
    }
}

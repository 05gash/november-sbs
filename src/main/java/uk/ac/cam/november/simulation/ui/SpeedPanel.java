package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class SpeedPanel extends JPanel {
    private static final long serialVersionUID = -2385072895131913349L;
    private WorldModel worldModel;

    public SpeedPanel(WorldModel worldModel) {
        this.worldModel = worldModel;
        setPreferredSize(new Dimension(250, 100));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();

        gr.setColor(Color.GRAY);

        gr.fillRect(0, 0, w, h);

        gr.setColor(Color.WHITE);
        String angleMessage = "Boat Speed: " + worldModel.getBoatSpeed();
        gr.drawString(angleMessage, 8, 16);

    }

}

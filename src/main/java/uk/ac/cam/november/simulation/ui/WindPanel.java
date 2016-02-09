package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class WindPanel extends JPanel {
    private static final long serialVersionUID = 4654100502287839615L;

    private WorldModel worldModel;

    public WindPanel(WorldModel worldModel) {
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
        String angleMessage = "Wind Angle: " + worldModel.getWindAngle();
        gr.drawString(angleMessage, 8, 16);
        String speedMessage = "Wind Speed: " + worldModel.getWindSpeed();
        gr.drawString(speedMessage, 8, 32);

    }

}

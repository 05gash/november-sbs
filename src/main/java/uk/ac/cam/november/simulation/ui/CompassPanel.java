package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class CompassPanel extends JPanel {
    private static final long serialVersionUID = 260790666737480725L;

    private WorldModel worldModel;

    public CompassPanel(WorldModel worldModel) {
        this.worldModel = worldModel;
        setPreferredSize(new Dimension(250, 250));
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        gr.setColor(Color.GRAY);
        int w = this.getWidth();
        int h = this.getHeight();
        gr.fillRect(0, 0, w, h);

        BufferedImage img = SimulatorUI.compassImage;

        AffineTransform matrix = new AffineTransform();
        matrix.translate(w / 2, h / 2);
        matrix.scale(0.75, 0.75);
        matrix.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        matrix.rotate(Math.toRadians(-worldModel.getHeading()), img.getWidth() / 2, img.getHeight() / 2);
        gr.drawImage(img, matrix, null);
    }

}

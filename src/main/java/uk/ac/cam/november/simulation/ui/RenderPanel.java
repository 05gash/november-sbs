package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.WorldModel;

public class RenderPanel extends JPanel {
    private static final long serialVersionUID = -6318987568691279660L;

    private WorldModel worldModel;

    public RenderPanel(WorldModel wm) {
        this.worldModel = wm;
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();

        gr.setColor(Color.CYAN);
        gr.fillRect(0, 0, w, h);

        BufferedImage img = SimulatorUI.boatImage;

        AffineTransform boatMatrix = new AffineTransform();
        boatMatrix.translate(w / 2, h / 2);
        boatMatrix.translate(worldModel.getBoatX(), worldModel.getBoatY());
        boatMatrix.scale(0.5, 0.5);
        boatMatrix.translate(-img.getWidth() / 2, -img.getHeight() / 2);
        boatMatrix.rotate(Math.toRadians(worldModel.getHeading()), img.getWidth() / 2, img.getHeight() / 2);
        gr.drawImage(SimulatorUI.boatImage, boatMatrix, null);
    }

}

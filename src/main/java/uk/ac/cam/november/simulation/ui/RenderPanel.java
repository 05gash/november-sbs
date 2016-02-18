package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JPanel;

import uk.ac.cam.november.simulation.DepthMap;
import uk.ac.cam.november.simulation.WorldModel;

public class RenderPanel extends JPanel {
    private static final long serialVersionUID = -6318987568691279660L;

    private WorldModel worldModel;
    private BufferedImage lakeImage;
    private Color backgroundColor;

    public RenderPanel(WorldModel wm) {
        this.worldModel = wm;

        backgroundColor = new Color(0x00001E);

        // Process depth map to produce a coloured lake image
        int height = DepthMap.depthMapImage.getHeight();
        int width = DepthMap.depthMapImage.getWidth();
        lakeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        int[] mask = DepthMap.depthMapImage.getRGB(0, 0, width, height, null, 0, width);
        int[] pixels = lakeImage.getRGB(0, 0, width, height, null, 0, width);

        Random rand = new Random();

        for (int i = 0; i < pixels.length; i++) {
            int alpha = mask[i] & 0xff; // use the green channel as 'grayscale'
            if (alpha == 255) { // dark green
                pixels[i] = (alpha << 24) | (0x425d00 + (rand.nextInt(30) << 8));
            } else if (alpha > 244) { // sand
                pixels[i] = ((10 * alpha - 2285) << 24) | 0xFFF0C2;
            } else { // dark blue
                pixels[i] = (alpha << 24) | 0x00009F;
            }
        }
        lakeImage.setRGB(0, 0, width, height, pixels, 0, width);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();

        gr.setColor(backgroundColor);
        gr.fillRect(0, 0, w, h);

        // Draw the lake
        AffineTransform lakeMatrix = new AffineTransform();
        lakeMatrix.translate(w / 2, h / 2);
        lakeMatrix.scale(2, 2);
        lakeMatrix.translate(-lakeImage.getWidth() / 2, -lakeImage.getHeight() / 2);
        lakeMatrix.translate(-worldModel.getBoatX(), -worldModel.getBoatY());
        gr.drawImage(lakeImage, lakeMatrix, null);

        // Draw the boat
        BufferedImage boatImg = SimulatorUI.boatImage;
        AffineTransform boatMatrix = new AffineTransform();
        boatMatrix.translate(w / 2, h / 2);
        boatMatrix.scale(0.25, 0.25);
        boatMatrix.translate(-boatImg.getWidth() / 2, -boatImg.getHeight() / 2);
        boatMatrix.rotate(Math.toRadians(worldModel.getHeading()), boatImg.getWidth() / 2, boatImg.getHeight() / 2);
        gr.drawImage(SimulatorUI.boatImage, boatMatrix, null);
    }

}

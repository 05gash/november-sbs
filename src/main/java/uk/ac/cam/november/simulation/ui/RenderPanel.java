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
    
    private double zoom = 2.0;

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
                pixels[i] = ((10 * alpha - 2285) << 24) | 0x2D9FCC;
            } else { // dark blue
                pixels[i] = (alpha << 24) | 0x00009F;
            }
        }
        lakeImage.setRGB(0, 0, width, height, pixels, 0, width);
    }

    public void setZoom(double z){
        zoom = z;
    }

    private final int MAX_SUBIMAGE_WIDTH = 3600;
    private final int MAX_SUBIMAGE_HEIGHT = 1800;

    private BufferedImage constructLakeSubimage(final BufferedImage lakeImage) {
        // Dealing with edge cases when boat is
        // close to the map border
        int xStart = lakeImage.getWidth() / 2 + ((int) worldModel.getBoatX()) - MAX_SUBIMAGE_WIDTH / 2;
        if (xStart < 0) {
            xStart = 0;
        }       
        int yStart = lakeImage.getHeight() / 2 + ((int) worldModel.getBoatY()) - MAX_SUBIMAGE_HEIGHT / 2;
        if (yStart < 0) {
            yStart = 0;
        }
        int width = lakeImage.getWidth() - xStart;
        if (width > MAX_SUBIMAGE_WIDTH) {
            width = MAX_SUBIMAGE_WIDTH;
        }
        int height = lakeImage.getHeight() - yStart;
        if (height > MAX_SUBIMAGE_HEIGHT) {
            height = MAX_SUBIMAGE_HEIGHT;
        }

        return lakeImage.getSubimage(xStart, yStart, width, height); 
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        int w = this.getWidth();
        int h = this.getHeight();

        gr.setColor(backgroundColor);
        gr.fillRect(0, 0, w, h);

        // Draw the lake
        if (zoom > 2.0) {
            final BufferedImage lakeSubimage = constructLakeSubimage(lakeImage);
            AffineTransform lakeMatrix = new AffineTransform();
            lakeMatrix.translate(w / 2, h / 2);
            lakeMatrix.scale(zoom, zoom);
            lakeMatrix.translate(-lakeSubimage.getWidth() / 2, -lakeSubimage.getHeight() / 2);
            gr.drawImage(lakeSubimage, lakeMatrix, null);
        } else {
            AffineTransform lakeMatrix = new AffineTransform();
            lakeMatrix.translate(w / 2, h / 2);
            lakeMatrix.scale(zoom, zoom);
            lakeMatrix.translate(-lakeImage.getWidth() / 2, -lakeImage.getHeight() / 2);
            lakeMatrix.translate(-worldModel.getBoatX(), -worldModel.getBoatY());
            gr.drawImage(lakeImage, lakeMatrix, null);
        }

        // Draw the boat
        BufferedImage boatImg = SimulatorUI.boatImage;
        AffineTransform boatMatrix = new AffineTransform();
        boatMatrix.translate(w / 2, h / 2);
        boatMatrix.scale(Math.sqrt(zoom) * 0.10, Math.sqrt(zoom) * 0.10);
        boatMatrix.translate(-boatImg.getWidth() / 2, -boatImg.getHeight() / 2);
        boatMatrix.rotate(Math.toRadians(worldModel.getHeading()), boatImg.getWidth() / 2, boatImg.getHeight() / 2);
        gr.drawImage(SimulatorUI.boatImage, boatMatrix, null);
    }

}

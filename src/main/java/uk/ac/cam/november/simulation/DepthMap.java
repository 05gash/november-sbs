package uk.ac.cam.november.simulation;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DepthMap {

    public static final float MIN_DEPTH_BOUNDARY = 0.0f;
    public static final float SHALLOW_DEPTH_BOUNDARY = 40.0f; 
    public static final float DEEP_DEPTH_BOUNDARY = 100.0f;

    // all pixel values above this boundary are considered
    // to model very deep water
    static final float DEEP_PIXEL_BOUNDARY = 200.0f;
    // all pixel values in the range [SHALLO_BOUNDARY, 255]
    // correspond to shallow water
    static final float SHALLOW_PIXEL_BOUNDARY = 240.0f;

    // if pixelIntensity = MIN_PIXEL_BOUNDARY, then
    // waterDepth = MIN_DEPTH_BOUNDARY
    static final float MIN_PIXEL_BOUNDARY = 255.0f;

    public float calculateDepth(final float pixelIntensity) {
        if (pixelIntensity < DEEP_PIXEL_BOUNDARY) {
                return DEEP_DEPTH_BOUNDARY;
        }
        if (pixelIntensity < SHALLOW_PIXEL_BOUNDARY) {
            return ((SHALLOW_PIXEL_BOUNDARY - pixelIntensity) / (SHALLOW_PIXEL_BOUNDARY - DEEP_PIXEL_BOUNDARY)) * 
                   (DEEP_DEPTH_BOUNDARY - SHALLOW_DEPTH_BOUNDARY) + SHALLOW_DEPTH_BOUNDARY;
        }

        return ((MIN_PIXEL_BOUNDARY - pixelIntensity) / (MIN_PIXEL_BOUNDARY - SHALLOW_PIXEL_BOUNDARY)) *
               (SHALLOW_DEPTH_BOUNDARY - MIN_DEPTH_BOUNDARY) + MIN_DEPTH_BOUNDARY;
    }




    public static final BufferedImage depthMapImage;
    private double scalex;
    private double scaley;
    private double offsetx;
    private double offsety;

    // Statically initialize the depth map image
    static {
        BufferedImage tmp = null;
        try {
            //tmp = ImageIO.read(DepthMap.class.getResource("/ui/bonylake_depth.png"));
            tmp = ImageIO.read(DepthMap.class.getResource("/ui/world_depth.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        depthMapImage = tmp;
    }

    public DepthMap(double scalex, double scaley) throws IOException {
        this.scalex = scalex;
        this.scaley = scaley;
        double w = scalex * depthMapImage.getWidth();
        double h = scaley * depthMapImage.getHeight();
        offsetx = w / 2;
        offsety = h / 2;
    }

    int getMapWidth() {
        return depthMapImage.getWidth();
    }
    
    int getMapHeight() {
        return depthMapImage.getHeight();
    }

    /**
     * Interpolate the depth map to produce a float depth at the given
     * coordinates.
     *
     * @param x
     *            The x-coordinate at which to perform the calculation. 0 is the
     *            centre of the image.
     * @param y
     *            The y-coordinate at which to perform the calculation. 0 is the
     *            centre of the image.
     * @return a float depth in meters. The range is set by the values of
     *         MIN_DEPTH and MAX_DEPTH;
     */
    public float getDepth(float x, float y) {
        // 0) Extracting pixel from png image

        // Pixel coordinates of the point in the depth map
        double pixelx = (x * scalex) + offsetx;
        double pixely = (y * scaley) + offsety;
        // Fractional pixel values, used for interpolation
        double ix = pixelx - (int) pixelx;
        double iy = pixely - (int) pixely;
        // pixel shifts to get next values
        int shiftx = pixelx < (depthMapImage.getHeight() - 1) ? 1 : 0;
        int shifty = pixely < (depthMapImage.getHeight() - 1) ? 1 : 0;
        // If we should be looking at the next pixel over, move over 1
        if (ix > 0.5) {
            pixelx += pixelx > 1 ? -1 : 0;
        }
        if (iy > 0.5) {
            pixely += pixely > 1 ? -1 : 0;
        }
        // Sum of pixel values
        double v1 = getGrayscale(depthMapImage.getRGB((int) pixelx, (int) pixely)) * (1 - ix) * (1 - iy);
        double v2 = getGrayscale(depthMapImage.getRGB((int) pixelx, (int) (pixely + shifty))) * (1 - ix) * iy;
        double v3 = getGrayscale(depthMapImage.getRGB((int) (pixelx + shiftx), (int) pixely)) * ix * (1 - iy);
        double v4 = getGrayscale(depthMapImage.getRGB((int) (pixelx + shiftx), (int) (pixely + shifty))) * ix * iy;

        // 1) We will now calculate water depth based on this pixel value
        double pixelIntensity = (v1 + v2 + v3 + v4);
        return calculateDepth((float) pixelIntensity);
    }

    /**
     * Calculate the average of the R, G and B channels of the pixel, given in
     * ARGB format.
     *
     * @param pixelColor
     *            integer color in ARGB format.
     * @return a double grayscale value.
     */
    private double getGrayscale(int pixelColor) {
        int r = (pixelColor >> 16) & 0xff;
        int g = (pixelColor >> 8) & 0xff;
        int b = pixelColor & 0xff;
        return (double) (r + g + b) / 3.0;
    }

}

package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Simple graph panel implementing a circular buffer of stored values.
 * 
 * @author Jamie Wood
 *
 */
public class GraphPanel extends JPanel {
    private static final long serialVersionUID = 852248389623269476L;

    public static final int FILLED = 1;
    public static final int LINE = 0;

    private int drawMode;

    private double[] vals;
    private int wx;

    private Color seabedColor;
    private Color seaColor;

    public GraphPanel(int numVals, int mode) {
        vals = new double[numVals];
        drawMode = mode;
        if (drawMode < 0 || drawMode > 1) {
            drawMode = 0;
        }
        setPreferredSize(new Dimension(numVals, 100));

        seabedColor = new Color(0x602901);
        seaColor = new Color(0x008FE1);
    }

    public void addValue(double v) {
        vals[wx] = v;
        wx += 1;
        if (wx >= vals.length) {
            wx = 0;
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D gr = (Graphics2D) g;

        gr.setColor(seabedColor);
        gr.fillRect(0, 0, getWidth(), getHeight());

        gr.setColor(seaColor);

        int start = wx;
        int end = wx - 1;
        if (end < 0) {
            end += vals.length;
        }
        int x = 0;
        int lastPoint = 0;
        for (int i = start; i != end; i++) {
            double val = vals[i];
            int vp = (int) (2*val);
            if (drawMode == FILLED) {
                gr.drawLine(x, vp, x, 0);
            } else if (drawMode == LINE) {
                if (x > 0) {
                    gr.drawLine(x - 1, lastPoint, x, vp);
                }
            }
            if (i == vals.length - 1) {
                i = -1;
            }
            lastPoint = vp;
            x++;
        }
    }

}

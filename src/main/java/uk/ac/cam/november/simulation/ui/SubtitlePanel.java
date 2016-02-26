package uk.ac.cam.november.simulation.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class SubtitlePanel extends JPanel {
    private static final long serialVersionUID = 473691366428419510L;

    private final Color TRANSPARENT = new Color(0, 0, 0, 0);
    private final Font SUBTITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private final long TIMEOUT = 4000L;

    private String subtitle = "";
    private long subtitletimeout;

    public void showSubtitle(String s) {
        subtitletimeout = System.currentTimeMillis() + TIMEOUT;
        subtitle = s;
    }

    @Override
    public void paint(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();

        g.setColor(TRANSPARENT);
        g.fillRect(0, 0, w, h);

        // remove the subtitle if its time has expired
        if (System.currentTimeMillis() > subtitletimeout) {
            subtitle = "";
        }

        if (subtitle.length() > 0) {
            g.setFont(SUBTITLE_FONT);

            Rectangle2D strSize = g.getFontMetrics().getStringBounds(subtitle, g);
            int sw = (int) strSize.getWidth();
            int sh = (int) strSize.getHeight();

            int sposx = (w - sw) / 2;
            int sposy = h - 50;

            g.setColor(Color.BLACK);
            g.fillRect(sposx - 10, sposy - sh - 10, sw + 20, sh + 20);
            g.setColor(Color.WHITE);
            g.drawString(subtitle, sposx, sposy);
        }
    }

}

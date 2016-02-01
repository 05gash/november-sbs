package uk.ac.cam.november.simulation.ui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.ac.cam.november.simulation.Simulator;

public class SimulatorUI extends JFrame {
    private static final long serialVersionUID = -3171613750699870243L;

    public static BufferedImage boatImage;

    private Simulator simulator;

    public SimulatorUI(Simulator sim) {
        super("Sailing by Sound Simulator");
        this.simulator = sim;

        loadImages();

        final RenderPanel panel = new RenderPanel(simulator.getWorldModel());

        panel.setFocusable(true);
        panel.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    simulator.getWorldModel().setBoatSpeed(simulator.getWorldModel().getBoatSpeed() + 1);
                    break;
                case KeyEvent.VK_S:
                    simulator.getWorldModel().setBoatSpeed(simulator.getWorldModel().getBoatSpeed() - 1);
                    break;
                case KeyEvent.VK_A:
                    simulator.getWorldModel().setHeading(simulator.getWorldModel().getHeading() - 3);
                    break;
                case KeyEvent.VK_D:
                    simulator.getWorldModel().setHeading(simulator.getWorldModel().getHeading() + 3);
                    break;
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
    }

    /**
     * Loads the images needed to render the user interface.
     */
    private void loadImages() {
        try {
            boatImage = ImageIO.read(getClass().getResource("/ui/boat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

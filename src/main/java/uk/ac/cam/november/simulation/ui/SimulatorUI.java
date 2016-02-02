package uk.ac.cam.november.simulation.ui;

import java.awt.BorderLayout;
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
    public static BufferedImage compassImage;

    private Simulator simulator;

    public SimulatorUI(Simulator sim) {
        super("Sailing by Sound Simulator");
        this.simulator = sim;

        loadImages();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        RenderPanel renderPanel = createRenderPanel();
        InstrumentPanel instrumentPanel = createInstrumentPanel();

        add(renderPanel, BorderLayout.CENTER);
        add(instrumentPanel, BorderLayout.EAST);

        revalidate();
    }

    private RenderPanel createRenderPanel() {
        RenderPanel renderPanel = new RenderPanel(simulator.getWorldModel());

        renderPanel.setFocusable(true);
        renderPanel.addKeyListener(new KeyListener() {
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
        return renderPanel;
    }

    private InstrumentPanel createInstrumentPanel(){
        InstrumentPanel instrumentPanel = new InstrumentPanel(simulator.getWorldModel());
        return instrumentPanel;
    }
    
    /**
     * Loads the images needed to render the user interface.
     */
    private void loadImages() {
        try {
            boatImage = ImageIO.read(getClass().getResource("/ui/boat.png"));
            compassImage = ImageIO.read(getClass().getResource("/ui/compass.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

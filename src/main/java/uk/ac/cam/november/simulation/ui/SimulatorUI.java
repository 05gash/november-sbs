package uk.ac.cam.november.simulation.ui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import uk.ac.cam.november.simulation.Simulator;

public class SimulatorUI extends JFrame {
    private static final long serialVersionUID = -3171613750699870243L;

    private SimulatorKeyListener keyListener;
    
    public static BufferedImage boatImage;
    public static BufferedImage compassImage;

    private Simulator simulator;
    
    public boolean KEY_UP;
    public boolean KEY_DOWN;
    public boolean KEY_LEFT;
    public boolean KEY_RIGHT;

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
        keyListener = new SimulatorKeyListener(this);
        renderPanel.addKeyListener(keyListener);
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

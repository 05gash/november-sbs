package uk.ac.cam.november.simulation.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SimulatorKeyListener implements KeyListener {

    private SimulatorUI ui;

    public SimulatorKeyListener(SimulatorUI ui) {
        this.ui = ui;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            ui.KEY_UP = true;
            break;
        case KeyEvent.VK_S:
            ui.KEY_DOWN = true;
            break;
        case KeyEvent.VK_A:
            ui.KEY_LEFT = true;
            break;
        case KeyEvent.VK_D:
            ui.KEY_RIGHT = true;
            break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
        case KeyEvent.VK_W:
            ui.KEY_UP = false;
            break;
        case KeyEvent.VK_S:
            ui.KEY_DOWN = false;
            break;
        case KeyEvent.VK_A:
            ui.KEY_LEFT = false;
            break;
        case KeyEvent.VK_D:
            ui.KEY_RIGHT = false;
            break;
        }
    }

}

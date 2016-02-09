package uk.ac.cam.november.messages;

import java.io.IOException;

public class MessageTest {
    public static void main(String[] args) {
        try {
            Message m = new Message("Warning. Collision imminent. 12 seconds to live. Emergency Mozart.", 1);
            MessageHandler.receiveMessage(m);
            Thread.sleep(2000);
            m = new Message("This is a test!", 1);
            MessageHandler.receiveMessage(m);
            Thread.sleep(3000);
            m = new Message("The cake was a lie", 1);
            MessageHandler.receiveMessage(m);
            Thread.sleep(700);
            m = new Message("Sorry for interrupting you", 1);
            MessageHandler.receiveMessage(m);
            Thread.sleep(1300);
            for (int i = 0; i < 5; i++) {
                m = new Message("No, I'm not!", 1);
                MessageHandler.receiveMessage(m);
                Thread.sleep(50);
            }
            Thread.sleep(500);
            m = new Message("And this should not be played!!", 0);
            MessageHandler.receiveMessage(m);
            Thread.sleep(500);
            m = new Message("And this is a bunch of priority", 4);
            MessageHandler.receiveMessage(m);
            Thread.sleep(500);
            m = new Message("And this will definitely not be played", 1);
            MessageHandler.receiveMessage(m);
            Thread.sleep(3000);
            m = new Message("This concludes the test!", 1);
            MessageHandler.receiveMessage(m);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException thrown");
        }

    }
}

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
            m = new Message("No, I'm not!", 1);
            MessageHandler.receiveMessage(m);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException thrown");
        }

    }
}

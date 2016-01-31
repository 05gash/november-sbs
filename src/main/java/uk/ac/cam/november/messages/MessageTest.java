package uk.ac.cam.november.messages;

import java.io.IOException;

public class MessageTest {
    public static void main(String[] args) {
        Message m = new Message("Warning. Collision imminent. 12 seconds to live. Emergency Mozart.", 1);
        MessageHandler.receiveMessage(m);
    }
}

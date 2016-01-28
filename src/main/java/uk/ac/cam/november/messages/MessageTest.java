package uk.ac.cam.november.messages;

import java.io.IOException;

public class MessageTest {
    public static void main(String[] args) {
        Message m = new Message("Hello, World!", 1);
        MessageHandler.receiveMessage(m);
    }
}

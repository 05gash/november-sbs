package uk.ac.cam.november.messages;

public class MessageHandler {

    // Attributes
    private static MessageHandler handlerSingleton = null; // Singleton pattern
    private Message currMessage;    // TODO: actually use this

    // Methods
    private MessageHandler() {
        // TODO: write something meaningful here
    }
    public static void receiveMessage(Message mess) {
        if (handlerSingleton == null) {
            handlerSingleton = new MessageHandler();
        }
        System.out.println("ABOUT TO PLAY!");
        SpeechSynthesis.play(mess.getText());
        // TODO: actually write logic and scheduling here
    }

}



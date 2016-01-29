package uk.ac.cam.november.messages;

public class MessageHandler {

    // Attributes
    private static Message currMessage;    // TODO: actually use this

    // Methods
    public static void receiveMessage(Message mess) {
        SpeechSynthesis.stop();
        SpeechSynthesis.play(mess.getText());
        // TODO: actually write logic and scheduling here
    }

}



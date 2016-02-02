package uk.ac.cam.november.messages;

public class MessageHandler {

    // Attributes
    private static Message currMessage;

    // Methods
    public static void receiveMessage(Message mess) {
        SpeechSynthesis.stop();
        SpeechSynthesis.play(mess.getText());
    }

}



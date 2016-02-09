package uk.ac.cam.november.messages;

/**
 * Handles {@link Message}s and considers them for text output.
 */
public class MessageHandler {

    // ATTRIBUTES

    private static Message currMessage;

    // METHODS

    /**
     * Passes a {@link Message} object to the message handler, which
     * in turn forwards it to the text-to-speech synthesis module.
     * The sound currently being played is possibly preempted.
     * <p>
     * The method returns as soon as the relevant processes are
     * finished.
     *
     * @param   message a {@link Message} object to be potentially spoken
     * @see             Message
     */
    public static void receiveMessage(Message message) {
        if (currMessage == null || message.getPriority() >= currMessage.getPriority()) {
            currMessage = message;
            SpeechSynthesis.stop();
            SpeechSynthesis.play(currMessage.getText());
        }
    }

}


package uk.ac.cam.november.messages;

import java.io.IOException;

public class SpeechSynthesis {

    private static Process curr_speech = null;
    private static String wavdir =
        "uk/ac/cam/november/messages/speech.wav";
    private static String scriptdir =
        "uk/ac/cam/november/messages/play_sound.sh";

    // Methods
    public static void play(String text) {
        try {
            curr_speech =
                (new ProcessBuilder(scriptdir, wavdir, text)).start();
        } catch (IOException e) {
            System.out.println("I/O exception: malformed script?");
        }
    }
    // This method stops the current sound being played.
    // Keep in mind that it has to be played within the same application!
    public static void stop() {
        if (curr_speech != null) {
            curr_speech.destroy();
            curr_speech = null;
        }
    }

}



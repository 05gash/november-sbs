package uk.ac.cam.november.messages;

import java.io.IOException;

public class SpeechSynthesis {

    private static Process curr_speech = null;

    // Methods
    public static void play(String text) {
        try {
            ProcessBuilder pb = new ProcessBuilder("espeak", "-ven+f3", "-k5",
                                                   "-s150", text);
            /* Nothing here? */
            /* TODO: consider using pico2wave & then playing */
            curr_speech = pb.start();
            // TODO: change this to something sensible
        } catch (IOException e) {
            System.out.println("I/O exception: malformed script?");
            // TODO: write something useful
        }
    }
    public static void stop() {
        if (curr_speech != null) {
            curr_speech.destroy();
        }
        // TODO: consider a better way to do this?
    }

}



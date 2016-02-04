package uk.ac.cam.november.messages;

import java.io.IOException;

/**
 * The module that handles audio output.
 * It is not concerned with the specifics of {@link Message} objects
 * and only handles Strings.
 * <p>
 * The user of this module is agnostic of the exact tool being used to
 * generate the sound. The default implementation uses pico2wave and
 * produces an auxiliary {@code speech.wav} file.
 */
public class SpeechSynthesis {

    // ATTRIBUTES

    private static Process curr_speech = null;
    private static String wavdir =
        "uk/ac/cam/november/messages/speech.wav";
    private static String playdir =
        "uk/ac/cam/november/messages/play_sound.sh";
    private static String stopdir =
        "uk/ac/cam/november/messages/play_sound.sh";

    // METHODS

    /**
     * Outputs audio for a given {@code String}.
     * <p>
     * If there happen to be errors in speech synthesis, a message is
     * written to {@code System.err}.
     * 
     * @param   text    {@code String} to be played
     */
    public static void play(String text) {
        try {
            curr_speech =
                (new ProcessBuilder(playdir, wavdir, text)).start();

        } catch (IOException e) {
            System.err.println("I/O exception: malformed play script?");
        }
    }
    
    /**
     * Stops the current sound being played.
     * <p>
     * Result predictable only if a single JVM is being run.
     */
    public static void stop() {
        if (curr_speech != null) {
            try {
                (new ProcessBuilder(stopdir, wavdir)).start();
            } catch (IOException e) {
                System.err.println("I/O exception raised from stop()");
            }
            curr_speech = null;
        }
    }

}



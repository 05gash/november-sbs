package uk.ac.cam.november.messages;

import java.io.IOException;

public class SpeechSynthesis {

    private static String command = "espeak -ven+f3 -k5 -s150 ";

    // Methods
    public static void play(String text) {
        try {
            // This currently has a bug: only the first word is read.
            // This will most likely be changed and not use Process,
            // or at the very least use different methods.
            String script = command + "\"" + text + "\"";
            Process p = Runtime.getRuntime().exec(script);
            p.waitFor(); // TODO: change this to something sensible
            // please please please don't pass anything with quotes!
        } catch (IOException e) {
            System.out.println("naughty naughty naughty IO");
            // TODO: write something useful
        } catch (InterruptedException e) {
            System.out.println("interrupted exception, should not happen now");
            // TODO: write something useful
        }
    }
    public static void stop() {
        // TODO: actually do something
    }

}



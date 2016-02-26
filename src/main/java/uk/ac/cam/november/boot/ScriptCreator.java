package uk.ac.cam.november.boot;

import java.io.File;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Module used on startup that generates important temporary files.
 * This class stores hardcoded information about the few small scripts
 * used for speech synthesis. Files are generated inside the the temp/
 * directory.
 * 
 *  @author
 */
public class ScriptCreator {

    private static String directoryName = "temp";
    private static String[] scriptName = {"play_sound.sh", "stop_sound.sh"};
    private static String[] scriptContents = {
        "pico2wave -w \"$1\" \"$2\" ; aplay \"$1\" \n",
        "killall \"$1\" \n"};
    
    /** Create all script files. */
    public static void writeScripts() throws IOException {
        int scriptCount = scriptName.length;
        File directory = new File(directoryName);
        if (!directory.exists()) {
            if (!directory.mkdir()) {
                throw new IOException();
            }
        }
        for (int i = 0; i < scriptCount; i++) {
            File file = new File("temp/" + scriptName[i]);
            FileWriter fout = new FileWriter(file);
            fout.write(scriptContents[i]);
            file.setExecutable(true);
            fout.close();
        }
    }
    
}

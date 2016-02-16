package uk.ac.cam.november.boot;

import java.io.File;
import java.io.FileWriter;
import java.io.DataOutputStream;
import java.io.IOException;

public class ScriptCreator {

    private static String[] scriptName = {"play_sound.sh", "stop_sound.sh"};
    private static String[] scriptContents = {
        "pico2wave -w \"$1\" \"$2\" ; aplay \"$1\" \n",
        "killall \"$1\" \n"};
        
    public static void writeScripts() throws IOException {
        int scriptCount = scriptName.length;
        for (int i = 0; i < scriptCount; i++) {
            File file = new File("temp/" + scriptName[i]);
            FileWriter fout = new FileWriter(file);
            fout.write(scriptContents[i]);
            file.setExecutable(true);
            fout.close();
        }
    }
    
}
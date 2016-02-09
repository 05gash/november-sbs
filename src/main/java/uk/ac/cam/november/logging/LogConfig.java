package uk.ac.cam.november.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

public class LogConfig {
    
    private static Logger LOGGER = Logger.getLogger("uk.ac.cam.november");
    private static final String LOG_FILE = "log.txt";
    private static final String XML_LOG_FILE = "xml_log.txt";
    
    public static void setup()
    {
        FileHandler fh  = null;
        FileHandler xfh = null;
        
        // initialize fh
        try {
            fh = new FileHandler(LOG_FILE,true);
                
        } catch (SecurityException e) {
            
            System.err.println("Cannot open file " + LOG_FILE);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            
            System.err.println("Cannot open file " + LOG_FILE);
            e.printStackTrace();
            return; 
        }
       
        //initialize xfh
        try {
            xfh = new FileHandler(XML_LOG_FILE,true);
                
        } catch (SecurityException e) {
            
            System.err.println("Cannot open file " + XML_LOG_FILE);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            
            System.err.println("Cannot open file " + XML_LOG_FILE);
            e.printStackTrace();
            return; 
        }
        
        
        fh.setFormatter(new SimpleFormatter());
        xfh.setFormatter(new XMLFormatter());
        
        LOGGER.addHandler(fh);
        LOGGER.addHandler(xfh);
        LOGGER.setLevel(Level.ALL);
    }
    
    public static void main(String args[])
    {
        LogConfig.setup();
        
        Logger l = Logger.getLogger("uk.ac.cam.november.logging.LogConfig");
        l.warning("Emergency Shostakovich ");
        l.fine("done");
    }
}
package uk.ac.cam.november.logging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.XMLFormatter;

/**
 * This class configures the logging for this program, setting the output 
 * files and format. It applies settings to the logger with the string
 * "uk.ac.cam.november", so all logs beginning with that prefix will inherit 
 * the properties.
 * 
 * @author Alan Tang
 * 
 */

public class LogConfig {
    
    private static Logger LOGGER = Logger.getLogger("uk.ac.cam.november");
    private static final String LOG_FILE = "log.txt";
    private static final String XML_LOG_FILE = "xml_log.txt";
    
    public static void setup()
    {
        FileHandler fh  = null;
        FileHandler xfh = null;
        
        /** Initializes File Handler */
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
       
        /** Initializes XFile Handler */
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
        
        /** Disable the default console handler on the root logger 
         * to prevent print looping from redirecting System.err */
        Logger rootLogger = Logger.getLogger("");
        rootLogger.removeHandler(rootLogger.getHandlers()[0]);
        
        /** Redirect System.out, System.err to double messages to logs */
        LoggerStream newOut = new LoggerStream(System.out, LOGGER, Level.INFO);
        LoggerStream newErr = new LoggerStream(System.err, LOGGER, Level.SEVERE);
        
        System.setOut(new PrintStream(newOut));
        System.setErr(new PrintStream(newErr));
    }
    
}

package uk.ac.cam.november.logging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A stream that redirects any output to also go to a specified logger, with one log message
 * printed after each new line character. The log messages are all set with a specified level.
 * @author Alan Tang
 *
 */
public class LoggerStream extends OutputStream{

    private Logger outputLogger;
    private Level  outputLevel;
    private OutputStream stream; 
    private StringBuilder logData;
    
    public LoggerStream(OutputStream out, Logger log, Level level) {
        super();
        stream = out;
        outputLogger = log;
        outputLevel  = level;
        logData = new StringBuilder();
    } 

    @Override
    public void write(int b) throws IOException {
        stream.write(b);
        
        if(b == '\n')
        {
            flush();
            logData = new StringBuilder();
        }
        else
        {
            logData.append((char) b);
        }
        
    }
    
    
    @Override
    public void flush() throws IOException{
        String [] src = getCaller();
        outputLogger.logp(outputLevel, src[0], src[1], logData.toString());
        super.flush();
    }

    private String[] getCaller()
    {
        Throwable throwable = new Throwable();
        StackTraceElement[] elements = throwable.getStackTrace();

        for (int i = 0; i < elements.length; i++) {

            StackTraceElement frame = elements[i];
            String cname = frame.getClassName();
            boolean isLogger = (cname.equals( "java.util.logging.Logger"));

            if (!isLogger) {
                if (!cname.startsWith("uk.ac.cam.november.logging")) {
                    if(cname.startsWith("uk.ac.cam.november"))
                    {  
                        String [] result = { cname, frame.getMethodName() };
                        return result;
                    }
                }
            }
        }
        String [] result = { "", "" };
        return result;
    }

}

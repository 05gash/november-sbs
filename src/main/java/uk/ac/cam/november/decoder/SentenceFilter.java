package uk.ac.cam.november.decoder;

import java.lang.InterruptedException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.sf.marineapi.nmea.sentence.Sentence;

// This class is used to create a priority system for
// decoding various data from different devices.
// The usage is very simple: if we want to read data from a device,
// we have to add its sentence ID into the hashmap by calling
// "addSentence(sentenceId)" method.
// if we want to stop polling some device, we have
// to call "blockSentence(sentenceId)" method.
// All devices that we want to poll will be polled
// simultaneously and the latest data will
// report the state.

class SentenceFilter {
    
    private final ConcurrentLinkedQueue<Sentence> incommingSentences = null;
    private final ConcurrentLinkedQueue<Sentence> outgoingSentences = new ConcurrentLinkedQueue<Sentence>();
    private final ConcurrentHashMap<String, Boolean> letThrough = new ConcurrentHashMap<String, Boolean>();

    public SentenceFilter() {
        Thread filter = new Thread() {
            public void run() {
                filterSentences();    
            }    
        };
        filter.setDaemon(true);
        filter.start();
    }
    
    // Ideally should call this method after class construction and
    // after several "letSenctenceThrough" calls have been made.
    // It is OK to call this method later as well, or to call it more than once.
    public void setIncommingQueue(final ConcurrentLinkedQueue<Sentence> incommingQueue) {
        incommingSentences = incommingQueue;
    } 
    
    public void addSentence(final String sentenceId) {
        letThrough.put(sentenceId, Boolean.TRUE);    
    }    

    public void blockSentence(final String sentenceId) {
        letThrough.put(sentenceId, Boolean.FALSE);    
    }

    public Queue<Sentence> getFilteredSentences() {
        return outgoingSentences;
    }

    public void filterSentences() {
        while (true) {
            while (incommingSentences == null || incommingSentences.isEmpty()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();    
                } 
            }
            final Sentence sentence = incommingSentences.poll();
            final String sentenceId = sentence.getSentenceId();
            final Boolean let = letThrough.get(sentenceId);
            if (let != null && let.booleanValue()) {
                outgoingSentences.add(sentence);
            }
        }
    } 
};

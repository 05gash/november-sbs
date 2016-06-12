/*
 * Just an example class to illustrate reading from a file.
 * Should not be used in production.
 */

package uk.ac.cam.november.decoder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.sf.marineapi.nmea.parser.SentenceParser;
import net.sf.marineapi.nmea.sentence.Sentence;

public class SentenceDecoderExample {

	public static void main(final String[] args) throws Exception {
		// 1) Creating incoming sentences queue.
		final ConcurrentLinkedQueue<Sentence> sentenceQueue = new ConcurrentLinkedQueue<Sentence>();
		BufferedReader reader = new BufferedReader(new FileReader("nmea_sentences.txt"));
		String line = reader.readLine();
		while (line != null) {
			try {
    			sentenceQueue.add(new SentenceParser(line));
            } catch (IllegalArgumentException exception) {
            	// Will throw exception once, as the first line of a file is incorrect.
            	exception.printStackTrace();
            } finally {
            	line = reader.readLine();
            }
		}
		reader.close();

		// 2) Creating a sentence filter
		final SentenceFilter sentenceFilter = new SentenceFilter();
		// test example
		sentenceFilter.letSentenceThrough("AP");
		sentenceFilter.setIncommingQueue(sentenceQueue);
		final Queue<Sentence> filteredQueue = sentenceFilter.getFilteredSentences();

		// 3) Reading filtered sentences
		while (true) {
			while (!filteredQueue.isEmpty()) {
				final Sentence filteredSentence = filteredQueue.poll();
				// For debugging
				System.out.println(filteredSentence.getSentenceId());
			}
			Thread.sleep(50);
		}
	}
}

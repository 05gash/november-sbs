package uk.ac.cam.november.connection;

import java.io.IOException;
import java.lang.Thread;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

// This class reveices all broadcasted messages as UDP packtes.

public class UDP {

    final private static int PORT = 10110;
    final private static int MAX_NMEA_SENTENCE_SIZE = 1024;  // Set to higher than max

    final private static byte[] BUFFER = new byte[MAX_NMEA_SENTENCE_SIZE];                                             
    final private static Queue<String> SENTENCE_QUEUE = new ConcurrentLinkedQueue<String>();
    
    public void receivePackets() {
        // Implemented as a thread as most likely packets
        // will be received asynchronously
        Thread thread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        DatagramSocket socket = new DatagramSocket(PORT);
                        DatagramPacket packet;
                        packet = new DatagramPacket(BUFFER, BUFFER.length);
                        socket.receive(packet);
                        String sentence = new String(packet.getData());
                        SENTENCE_QUEUE.add(sentence);
                    }
                    catch (IOException exception) {
                        exception.printStackTrace();
                    }         
                }
            }
        };

        thread.setDaemon(true);
        thread.start();
    }

    static public Queue<String> getQueue() {
        return SENTENCE_QUEUE;
    }
                            
}

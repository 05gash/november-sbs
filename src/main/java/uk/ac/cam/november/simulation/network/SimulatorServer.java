package uk.ac.cam.november.simulation.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;

import uk.ac.cam.november.messages.Message;
import uk.ac.cam.november.messages.MessageHandler;
import uk.ac.cam.november.messages.SpeechListener;
import uk.ac.cam.november.messages.SpeechSynthesis;
import uk.ac.cam.november.packet.Packet;

public class SimulatorServer implements SpeechListener {

    private ServerSocket listenSocket;
    private Socket client;
    private DataOutputStream dos;

    private Queue<Packet> messageQueue;

    public SimulatorServer() {

        EvictingQueue<Packet> pq = EvictingQueue.create(300);
        messageQueue = Queues.synchronizedQueue(pq);

        SpeechSynthesis.addSpeechListener(this);

        try {
            listenSocket = new ServerSocket(8989);
        } catch (IOException e) {
            System.err.println("Failed to bind port!");
            System.err.println("ERROR: " + e.getMessage());
            throw new RuntimeException("Failed to start server.");
        }

        Thread socketThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        client = listenSocket.accept();
                        dos = new DataOutputStream(client.getOutputStream());

                        System.out.println("New client connected from " + client.getInetAddress().getHostName());
                        MessageHandler.receiveMessage(new Message("Client connected", 2));

                        DataInputStream dis = new DataInputStream(client.getInputStream());
                        while (true) {
                            Packet p = PacketTranslator.read(dis);
                            if (p != null) {
                                queueMessage(p);
                            } else {
                                break;
                            }
                        }
                        client.close();
                        client = null;
                        System.out.println("Client disconnected.");
                        MessageHandler.receiveMessage(new Message("Client disconnected", 2));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        socketThread.start();

        System.out.println("Server started on port 8989");
        MessageHandler.receiveMessage(new Message("Server started on port 8989", 2));
    }

    /**
     * Returns the message queue used to retrieve messages.
     * 
     * The returned queue is a thread-safe circular buffer of size 300.
     * 
     * @return Queue<Packet> the message queue.
     */
    public Queue<Packet> getMessageQueue() {
        return messageQueue;
    }

    /**
     * Adds a message to the output queue.
     * 
     * @param p
     *            The message to add.
     */
    public void queueMessage(Packet p) {
        System.out.println("Queueing a " + p.getDescription() + " packet at " + p.getTimestamp());
        messageQueue.add(p);
    }

    @Override
    public void onSpeechStarted(String message) {
        if (client != null) {
            SubtitlePacket sp = new SubtitlePacket(message);
            try {
                sp.write(dos);
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    client.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}

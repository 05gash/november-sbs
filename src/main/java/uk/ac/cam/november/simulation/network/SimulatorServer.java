package uk.ac.cam.november.simulation.network;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;

import uk.ac.cam.november.messages.Message;
import uk.ac.cam.november.messages.MessageHandler;
import uk.ac.cam.november.packet.Packet;

public class SimulatorServer {

    private ServerSocket listenSocket;

    private Queue<Packet> messageQueue;

    public SimulatorServer() {

        // truly hideous hack, part 1
        try {
            File file = new File("temp/subtitle.py");
            FileWriter fout = new FileWriter(file);
            fout.write(
                    "import socket, sys; s=socket.socket(socket.AF_INET, socket.SOCK_STREAM); s.connect((sys.argv[1], 8988)); s.send(sys.argv[2]); s.close()");
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //

        EvictingQueue<Packet> pq = EvictingQueue.create(300);
        messageQueue = Queues.synchronizedQueue(pq);

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
                        Socket client = listenSocket.accept();
                        System.out.println("New client connected from " + client.getInetAddress().getHostName());
                        MessageHandler.receiveMessage(new Message("Client connected", 2));

                        // truly hideous hack, part 2
                        String newcode = "python temp/subtitle.py \"" + client.getInetAddress().getHostAddress()
                                + "\" \"$2\" ; pico2wave -w \"$1\" \"$2\" ; aplay \"$1\" \n";
                        File file = new File("temp/play_sound.sh");
                        FileWriter fout = new FileWriter(file);
                        fout.write(newcode);
                        file.setExecutable(true);
                        fout.close();
                        //

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

}

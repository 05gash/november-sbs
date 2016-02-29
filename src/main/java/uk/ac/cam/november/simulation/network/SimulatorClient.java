package uk.ac.cam.november.simulation.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import uk.ac.cam.november.packet.Packet;
import uk.ac.cam.november.simulation.ui.SimulatorUI;

public class SimulatorClient {

    private Socket socket;
    private DataOutputStream dos;

    public SimulatorClient(SimulatorUI ui, String addr) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(addr);

        socket = new Socket(serverAddress, 8989);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    while (true) {
                        int packetid = dis.readByte();

                        if (packetid == SubtitlePacket.PACKET_ID) {
                            SubtitlePacket sp = new SubtitlePacket(dis);
                            ui.showSubtitle(sp.getSubtitle());
                        } else {
                            throw new IOException("Unknown packet type received: (" + packetid + ")");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                    } catch (IOException e1) {
                    }
                }
            }
        });
        t.start();

        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void sendPacket(Packet p) throws IOException {
        PacketTranslator.write(dos, p);
    }

    public void close() {
        try {
            dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

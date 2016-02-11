package uk.ac.cam.november.simulation.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import uk.ac.cam.november.packet.Packet;

public class SimulatorClient {

    private Socket socket;

    private DataOutputStream dos;

    public SimulatorClient(String addr) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(addr);

        socket = new Socket(serverAddress, 8989);

        dos = new DataOutputStream(socket.getOutputStream());
    }

    public void sendPacket(Packet p) {
        PacketTranslator.write(dos, p);
    }

}

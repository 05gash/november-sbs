package uk.ac.cam.november.simulation.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import uk.ac.cam.november.packet.Packet;
import uk.ac.cam.november.simulation.ui.SimulatorUI;

public class SimulatorClient {

    private Socket socket;

    private DataOutputStream dos;

    public SimulatorClient(SimulatorUI ui, String addr) throws IOException {
        InetAddress serverAddress = InetAddress.getByName(addr);

        socket = new Socket(serverAddress, 8989);

        final ServerSocket ss = new ServerSocket(8988);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket s = ss.accept();
                        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        ui.showSubtitle(sb.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
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

package uk.ac.cam.november.simulation.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import uk.ac.cam.november.packet.Packet;

public class PacketTranslator {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd-HH:mm:ss.SSSSS").create();

    public static void write(DataOutputStream dos, Packet p) throws IOException {
        String data = gson.toJson(p);
        byte[] dataBytes = data.getBytes("ASCII");
        dos.writeInt(dataBytes.length);
        dos.write(dataBytes);
    }

    public static Packet read(DataInputStream dos) {
        Packet packet = null;
        try {
            int len = dos.readInt();
            byte[] buff = new byte[len];
            dos.readFully(buff, 0, len);
            String json = new String(buff, "ASCII");
            packet = gson.fromJson(json, Packet.class);
        } catch (IOException e) {
            // socket died, let caller decide what to do
        }
        return packet;
    }

}

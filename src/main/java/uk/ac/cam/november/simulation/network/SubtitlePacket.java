package uk.ac.cam.november.simulation.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SubtitlePacket {

    public static final byte PACKET_ID = 0x01;
    
    private String subtitle;
    byte buffer[] = new byte[4096];

    public SubtitlePacket(String s) {
        subtitle = s;
    }

    public SubtitlePacket(DataInputStream dis) throws IOException {
        int strlen = dis.readInt();

        if (strlen > buffer.length) {
            throw new IOException("Packet too long for buffer: (" + strlen + ")");
        }

        dis.read(buffer, 0, strlen);
        subtitle = new String(buffer, 0, strlen, "ASCII");
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void write(DataOutputStream dos) throws IOException {
        dos.writeByte(PACKET_ID);
        dos.writeInt(subtitle.length());
        byte buf[] = subtitle.getBytes("ASCII");
        dos.write(buf);
    }

}

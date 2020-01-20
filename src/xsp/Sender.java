package xsp;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author m1kc
 */
public class Sender implements XSPConstants {

    public static void sendPacket(DataOutputStream os, int type, int subtype, UIProxy u) {
        sendPacket(os, type, subtype, (String[]) null, null, u);
    }

    public static void sendPacket(DataOutputStream os, int type, int subtype, String utf, byte[] bytes, UIProxy u) {
        sendPacket(os, type, subtype, (utf == null ? (String[]) null : new String[]{utf}), bytes, u);
    }

    public static void sendPacket(DataOutputStream os, int type, int subtype, String[] utf, byte[] bytes, UIProxy u) {
        try {
            os.writeInt(type);
            os.writeInt(subtype);

            if (utf == null) {
                os.writeInt(0);
            } else {
                os.writeInt(utf.length);
                for (String s : utf) {
                    os.writeUTF(s);
                }
            }

            if (bytes == null) {
                os.writeInt(0);
            } else {
                os.writeInt(bytes.length);
                os.write(bytes);
            }

            os.flush();
        } catch (IOException ex) {
            u.errorWhileSending(ex);
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.packetSent(type, subtype, utf, bytes);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m1kc
 */
public class Sender implements XSPConstants
{

    public static void sendUTF(DataOutputStream os, int type, String body, UIProxy u)
    {
        try {
            os.writeByte(TEXT);
            os.writeInt(type);
            if (body==null) os.writeUTF(""); else os.writeUTF(body);
            os.flush();
        } catch (IOException ex) {
            u.errorWhileSending(ex);
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.textSent(type, body);
    }

    public static void sendBytes(DataOutputStream os, int type, byte[] body, UIProxy u)
    {
        try {
            os.writeByte(BINARY);
            os.writeInt(type);
            os.writeInt(body.length);
            if (body==null) os.write(new byte[0]); else os.write(body);
            os.flush();
        } catch (IOException ex) {
            u.errorWhileSending(ex);
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.binarySent(type, body);
    }
}

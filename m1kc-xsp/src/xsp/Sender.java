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

    /**
     * @deprecated
     */
    public static void sendUTF(DataOutputStream os, int type, String body, UIProxy u)
    {
        sendPack(os, type, body, null, u);
    }

    /**
     * @deprecated
     */
    public static void sendBytes(DataOutputStream os, int type, byte[] body, UIProxy u)
    {
        sendPack(os, type, (String[])null, body, u);
    }

    public static void sendPack(DataOutputStream os, int type, UIProxy u)
    {
        sendPack(os, type, (String[])null, null, u);
    }

    public static void sendPack(DataOutputStream os, int type, String utf, byte[] bytes, UIProxy u)
    {
        if (utf == null) sendPack(os, type, (String[]) null, bytes, u);
        else sendPack(os, type, new String[]{utf}, bytes, u);
    }

    public static void sendPack(DataOutputStream os, int type, String[] utf, byte[] bytes, UIProxy u)
    {
        try {
            os.writeInt(type);

            if (utf==null)
            {
                os.writeInt(0);
            }
            else
            {
                os.writeInt(utf.length);
                for (int i=0; i<utf.length; i++) os.writeUTF(utf[i]);
            }

            if (bytes==null)
            {
                os.writeInt(0);
            }
            else
            {
                os.writeInt(bytes.length);
                os.write(bytes);
            }

            os.flush();
        } catch (IOException ex) {
            u.errorWhileSending(ex);
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.packSent(type, utf, bytes);
    }
}

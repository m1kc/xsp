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

    public static void sendPack(DataOutputStream os, int type, int subtype, UIProxy u)
    {
        sendPack(os, type, subtype, (String[])null, null, u);
    }

    public static void sendPack(DataOutputStream os, int type, int subtype, String utf, byte[] bytes, UIProxy u)
    {
        if (utf == null) sendPack(os, type, subtype, (String[]) null, bytes, u);
        else sendPack(os, type, subtype, new String[]{utf}, bytes, u);
    }

    public static void sendPack(DataOutputStream os, int type, int subtype, String[] utf, byte[] bytes, UIProxy u)
    {
        try {
            os.writeInt(type);
            os.writeInt(subtype);

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
        u.packSent(type, subtype, utf, bytes);
    }
}

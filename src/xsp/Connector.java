/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author m1kc
 */
public class Connector
{
    public static Session connectAsServer(int port, UIProxy u)
    {
        Socket mainSocket = null;
        Socket fileSocket = null;
        Socket voiceSocket = null;
        Socket screenSocket = null;
        try {
            ServerSocket server = new ServerSocket(port);
            mainSocket = server.accept();
            fileSocket = server.accept();
            voiceSocket = server.accept();
            screenSocket = server.accept();
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        Session ss = new Session(mainSocket, fileSocket, voiceSocket, screenSocket, u);
        ss.start();
        return ss;
    }

    public static Session connectAsClient(String host, int port, UIProxy u)
    {
        Socket mainSocket = null;
        Socket fileSocket = null;
        Socket voiceSocket = null;
        Socket screenSocket = null;
        try {
            mainSocket = new Socket(host, port);
            fileSocket = new Socket(host, port);
            voiceSocket = new Socket(host, port);
            screenSocket = new Socket(host, port);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Connector.class.getName()).log(Level.SEVERE, null, ex);
        }
        Session ss = new Session(mainSocket, fileSocket, voiceSocket, screenSocket, u);
        ss.start();
        return ss;
    }
}

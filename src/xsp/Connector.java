package xsp;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author m1kc
 */
public class Connector {

    public static Session connectAsServer(int port, UIProxy u) throws IOException {
        ServerSocket server = new ServerSocket(port);
        Socket mainSocket = server.accept();
        Socket fileSocket = server.accept();
        Socket voiceSocket = server.accept();
        Socket screenSocket = server.accept();
        Session ss = new Session(mainSocket, fileSocket, voiceSocket, screenSocket, u);
        ss.start();
        return ss;
    }

    public static Session connectAsClient(String host, int port, UIProxy u) throws IOException {
        Socket mainSocket = new Socket(host, port);
        Socket fileSocket = new Socket(host, port);
        Socket voiceSocket = new Socket(host, port);
        Socket screenSocket = new Socket(host, port);
        Session ss = new Session(mainSocket, fileSocket, voiceSocket, screenSocket, u);
        ss.start();
        return ss;
    }
}

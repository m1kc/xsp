/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.io.*;
import java.net.*;
import java.util.logging.*;

/**
 *
 * @author m1kc
 */
public class Session extends Thread implements XSPConstants
{
    Socket socket,fileSocket,voiceSocket,screenSocket;
    public DataInputStream is, fis, vis, sis;
    public DataOutputStream os, fos, vos, sos;
    public UIProxy uiproxy;

    public Session(Socket socket, Socket fileSocket, Socket voiceSocket, Socket screenSocket, UIProxy uiproxy)
    {
        this.socket = socket;
        this.fileSocket = fileSocket;
        this.voiceSocket = voiceSocket;
        this.screenSocket = screenSocket;
        this.uiproxy = uiproxy;

        this.setName("XSP Session");

        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            fis = new DataInputStream(fileSocket.getInputStream());
            fos = new DataOutputStream(fileSocket.getOutputStream());
            vis = new DataInputStream(voiceSocket.getInputStream());
            vos = new DataOutputStream(voiceSocket.getOutputStream());
            sis = new DataInputStream(screenSocket.getInputStream());
            sos = new DataOutputStream(screenSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run()
    {
        while(true)
        {
            try {
                if (is.available() > 0) {

                    // Next generation:
                    // [int:тип][int:подтип][int:кол-во UTF][UTF]...[UTF][int:кол-во байт][byte[]:байты]

                    // Прочитать тип пакета
                    int type = is.readInt();
                    // Прочитать подтип
                    int subtype = is.readInt();
                    // Читаем UTF
                    int utfl = is.readInt();
                    String[] utf = null;
                    if (utfl>0) utf = new String[utfl];
                    if (utf != null) for (int i=0; i<utf.length; i++) utf[i]=is.readUTF();
                    // Байты
                    int bytel = is.readInt();
                    byte[] bytes = null;
                    if (bytel>0) bytes = new byte[bytel];
                    if (bytes != null)
                    {
                        int received = 0;
                        while (received < bytel)
                        {
                            received += is.read(bytes, received, bytel-received);
                        }
                    }
                    uiproxy.packReceived(type, subtype, utf, bytes);
                    callHandler(type, subtype, utf, bytes);
                }
            } catch (Throwable ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void callHandler(int type, int subtype, String[] body, byte[] bytes)
    {
        switch(type)
        {
            case SERVICE:
                uiproxy.handleService(subtype, body, bytes);
                break;

            case PING:
                uiproxy.handlePing(subtype, body, bytes);
                break;
            case CAPSCHECK:
                uiproxy.handleCapsCheck(subtype, body, bytes);
                break;
            case MESSAGE:
                uiproxy.handleMessage(subtype, body, bytes);
                break;
            case TERMINAL:
                uiproxy.handleTerminal(subtype, body, bytes);
                break;
            case FILE:
                uiproxy.handleFile(subtype, body, bytes);
                break;
            case MICROPHONE:
                uiproxy.handleMicrophone(subtype, body, bytes);
                break;
            case DIALOG:
                uiproxy.handleDialog(subtype, body, bytes);
                break;
            case MOUSE:
                uiproxy.handleMouse(subtype, body, bytes);
                break;

            default:
                uiproxy.errorUnknownType(type, subtype);
                break;
        }
    }

}

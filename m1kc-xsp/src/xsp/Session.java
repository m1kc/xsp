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
    Socket socket,fileSocket,voiceSocket;
    public DataInputStream is, fis, vis;
    public DataOutputStream os, fos, vos;
    public UIProxy uiproxy;

    public Session(Socket socket, Socket fileSocket, Socket voiceSocket, UIProxy uiproxy)
    {
        this.socket = socket;
        this.fileSocket = fileSocket;
        this.voiceSocket = voiceSocket;
        this.uiproxy = uiproxy;

        this.setName("XSP Session");

        try {
            is = new DataInputStream(socket.getInputStream());
            os = new DataOutputStream(socket.getOutputStream());
            fis = new DataInputStream(fileSocket.getInputStream());
            fos = new DataOutputStream(fileSocket.getOutputStream());
            vis = new DataInputStream(voiceSocket.getInputStream());
            vos = new DataOutputStream(voiceSocket.getOutputStream());
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
                    // [int:тип][int:кол-во UTF][UTF]...[UTF][int:кол-во байт][byte[]:байты]

                    // Прочитать тип пакета
                    int type = is.readInt();
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
                    uiproxy.packReceived(type, utf, bytes);
                    callHandler(type, utf, bytes);

                    /*
                     *
                    // Прочитать вид пакета
                    byte look = is.readByte();
                    // Прочитать тип пакета
                    int type = is.readInt();
                    switch(look)
                    {
                        case TEXT:
                            // Прочитать тело пакета
                            String sbody = is.readUTF();
                            // Оповестить
                            uiproxy.textReceived(type,sbody);
                            // Обработать
                            callTextHandler(type,sbody);
                            break;
                        case BINARY:
                            // Прочитать длину пакета
                            int length = is.readInt();
                            // Прочитать тело пакета
                            byte[] bbody = new byte[length];
                            int received = 0;
                            while (received < length)
                            {
                                received += is.read(bbody, received, length-received);
                            }
                            //is.read(bbody);
                            // Оповестить
                            uiproxy.binaryReceived(type,bbody);
                            // Обработать
                            callBinaryHandler(type,bbody);
                            break;
                        default:
                            // Неизвестный вид пакета! Шоковое чтение.
                            while (is.available()>0) is.read();
                            uiproxy.errorUnknownLook(look);
                            break;
                    }
                     *
                     */
                }
            } catch (IOException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void callHandler(int type, String[] body, byte[] bytes)
    {
        // KEEP IT SIMPLE, STUPID!!!
        // Ради скорости убрал класс обработчика пакетов. Пусть их обрабатывает
        // uiproxy. Я понимаю, что масштабируемость, но когда я подумал,
        // сколько это классов выйдет, я чуть не повесился.
        switch(type)
        {
            case OK:
                uiproxy.handleOK(body, bytes);
                break;
            case INVALID:
                uiproxy.handleInvalid(body, bytes);
                break;
            case ERROR:
                uiproxy.handleError(body, bytes);
                break;
            case REFUSED:
                uiproxy.handleRefused(body, bytes);
                break;

            case PING:
                uiproxy.handlePing(body, bytes);
                break;
            case CAPSCHECK:
                uiproxy.handleCapsCheck(body, bytes);
                break;
            case MESSAGE:
                uiproxy.handleMessage(body, bytes);
                break;
            case TERMINAL:
                uiproxy.handleTerminal(body, bytes);
                break;
            case FILERQ:
                uiproxy.handleFileRq(body, bytes);
                break;
            case MICROPHONERQ:
                uiproxy.handleMicrophoneRq(body, bytes);
                break;
            case DIALOGRQ:
                uiproxy.handleDialogRq(body, bytes);
                break;
            case MICROPHONESTOP:
                uiproxy.handleMicrophoneStop(body, bytes);
                break;
            case DIALOGSTOP:
                uiproxy.handleDialogStop(body, bytes);
                break;
            case MOUSE:
                uiproxy.handleMouse(body, bytes);
                break;

            default:
                uiproxy.errorUnknownType(type);
                break;
        }
    }

}

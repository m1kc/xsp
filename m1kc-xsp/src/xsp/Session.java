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
                    // Еще раз, структура пакета:
                    // [byte:вид][int:тип][содержимое]
                    // В итоге:
                    // [byte:1][int:тип][UTF:содержимое]
                    // или
                    // [byte:2][int:тип][int:длина][bytes:содержимое]

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

    private void callTextHandler(int type, String body)
    {
        // KEEP IT SIMPLE, STUPID!!!
        // Ради скорости убрал класс обработчика пакетов. Пусть их обрабатывает
        // uiproxy. Я понимаю, что масштабируемость, но когда я подумал,
        // сколько это классов выйдет, я чуть не повесился.
        switch(type)
        {
            case OK:
                uiproxy.handleOK(body);
                break;
            case INVALID:
                uiproxy.handleInvalid(body);
                break;
            case ERROR:
                uiproxy.handleError(body);
                break;
            case REFUSED:
                uiproxy.handleRefused(body);
                break;

            case PING:
                uiproxy.handlePing(body);
                break;
            case CAPSCHECK:
                uiproxy.handleCapsCheck(body);
                break;
            case MESSAGE:
                uiproxy.handleMessage(body);
                break;
            case TERMINAL:
                uiproxy.handleTerminal(body);
                break;
            case FILERQ:
                uiproxy.handleFileRq(body);
                break;
            case MICROPHONERQ:
                uiproxy.handleMicrophoneRq(body);
                break;
            case DIALOGRQ:
                uiproxy.handleDialogRq(body);
                break;
            case MICROPHONESTOP:
                uiproxy.handleMicrophoneStop(body);
                break;
            case DIALOGSTOP:
                uiproxy.handleDialogStop(body);
                break;
            case MOUSE:
                uiproxy.handleMouse(body);
                break;

            default:
                uiproxy.errorUnknownTextType(type);
                break;
        }
    }

    private void callBinaryHandler(int type, byte[] body)
    {
        switch(type)
        {
            case FILEPART:
                uiproxy.handleFilePart(body);
                break;

            default:
                uiproxy.errorUnknownBinaryType(type);
                break;
        }
    }
}

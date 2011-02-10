/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

/**
 *
 * @author m1kc
 */
public interface UIProxy
{
    // Стандартные функции

    // Ошибки

    public void errorWhileSending(Throwable ex);

    // Обработка пакетов

    public void textReceived(int type, String sbody);
    public void binaryReceived(int type, byte[] bbody);
    public void textSent(int type, String sbody);
    public void binarySent(int type, byte[] bbody);

    // Обработка ошибочных пакетов

    public void errorUnknownLook(byte look);
    public void errorUnknownTextType(int type);
    public void errorUnknownBinaryType(int type);

    // Обработка текстовых пакетов

    public void handleOK(String body);
    public void handleInvalid(String body);
    public void handleError(String body);
    public void handleRefused(String body);
    public void handlePing(String s);
    public void handleCapsCheck(String body);
    public void handleMessage(String s);
    public void handleTerminal(String body);
    public void handleFileRq(String body);
    public void handleFileDone(String body);
    public void handleMicrophoneRq(String body);
    public void handleDialogRq(String body);
    public void handleMicrophoneStop(String body);
    public void handleDialogStop(String body);
    public void handleMouse(String body);

    // Обработка бинарных пакетов

    public void handleFilePart(byte[] body);

    // DirectTransfer

    public void sendProgress(long sent, long size, int speed);
    public void sendDone();
    public void receiveProgress(long got, long size, int speed);
    public void receiveDone();

}

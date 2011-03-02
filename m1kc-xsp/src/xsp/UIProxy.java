/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.awt.image.BufferedImage;

/**
 *
 * @author m1kc
 */
public interface UIProxy
{
    // Ошибки

    public void errorWhileSending(Throwable ex);

    // Обработка пакетов

    public void packReceived(int type, int subtype, String[] utf, byte[] bytes);
    public void packSent(int type, int subtype, String[] utf, byte[] bytes);

    // Обработка ошибочных пакетов

    public void errorUnknownType(int type, int subtype);

    // Обработка пакетов

    public void handleOK(int subtype, String[] body, byte[] bytes);
    public void handleInvalid(int subtype, String[] body, byte[] bytes);
    public void handleError(int subtype, String[] body, byte[] bytes);
    public void handleRefused(int subtype, String[] body, byte[] bytes);
    public void handlePing(int subtype, String[] s, byte[] bytes);
    public void handleCapsCheck(int subtype, String[] body, byte[] bytes);
    public void handleMessage(int subtype, String[] s, byte[] bytes);
    public void handleTerminal(int subtype, String[] body, byte[] bytes);
    public void handleFileRq(int subtype, String[] body, byte[] bytes);
    public void handleMicrophoneRq(int subtype, String[] body, byte[] bytes);
    public void handleDialogRq(int subtype, String[] body, byte[] bytes);
    public void handleMicrophoneStop(int subtype, String[] body, byte[] bytes);
    public void handleDialogStop(int subtype, String[] body, byte[] bytes);
    public void handleMouse(int subtype, String[] body, byte[] bytes);
    public void handleScreen(int subtype, String[] body, byte[] bytes);

    // DirectTransfer

    public void sendProgress(long sent, long size, int speed);
    public void sendDone();
    public void receiveProgress(long got, long size, int speed);
    public void receiveDone();

    // ScreenStreaming

    public void screenUpdated(BufferedImage inputBuf);

}

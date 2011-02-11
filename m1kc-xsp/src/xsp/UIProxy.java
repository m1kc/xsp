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
    // Ошибки

    public void errorWhileSending(Throwable ex);

    // Обработка пакетов

    public void packReceived(int type, String[] utf, byte[] bytes);
    public void packSent(int type, String[] utf, byte[] bytes);

    // Обработка ошибочных пакетов

    public void errorUnknownType(int type);

    // Обработка пакетов

    public void handleOK(String[] body, byte[] bytes);
    public void handleInvalid(String[] body, byte[] bytes);
    public void handleError(String[] body, byte[] bytes);
    public void handleRefused(String[] body, byte[] bytes);
    public void handlePing(String[] s, byte[] bytes);
    public void handleCapsCheck(String[] body, byte[] bytes);
    public void handleMessage(String[] s, byte[] bytes);
    public void handleTerminal(String[] body, byte[] bytes);
    public void handleFileRq(String[] body, byte[] bytes);
    public void handleMicrophoneRq(String[] body, byte[] bytes);
    public void handleDialogRq(String[] body, byte[] bytes);
    public void handleMicrophoneStop(String[] body, byte[] bytes);
    public void handleDialogStop(String[] body, byte[] bytes);
    public void handleMouse(String[] body, byte[] bytes);
    public void handleScreen(String[] body, byte[] bytes);

    // DirectTransfer

    public void sendProgress(long sent, long size, int speed);
    public void sendDone();
    public void receiveProgress(long got, long size, int speed);
    public void receiveDone();

}

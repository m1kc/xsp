/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.io.*;
import java.util.logging.*;

/**
 *
 * @author m1kc
 */
public class DirectTransfer
{
    public static void sendFile(String filename, OutputStream os, long start, UIProxy u)
    {
        try {
            File f = new File(filename);
            DataOutputStream dos = new DataOutputStream(os);
            FileInputStream fis = new FileInputStream(f);
            // Протокол: имя файла
            dos.writeUTF(f.getName());
            // Протокол: размер
            long size = fis.available();
            dos.writeLong(size);
            // Протокол: начальная точка
            dos.writeLong(start);
            // Протокол: файл
            if (start > 0) {
                fis.skip(start);
            }
            long sent = 0;
            long starttime = System.currentTimeMillis();
            long speed = 0, time = 0, fulltime = 0, rest = 0;
            byte[] b = new byte[1024 * 128];
            while (fis.available() > 0) {
                if (fis.available() < b.length) {
                    b = new byte[fis.available()];
                }
                fis.read(b);
                dos.write(b);
                dos.flush();
                sent += b.length;

                // Цифры
                time = System.currentTimeMillis() - starttime; // Время в мс

                if (time==0) speed=0;
                else speed = 1000 * sent / time; // байты в секунду

                fulltime = time * size / sent; // Полное время в мс
                rest = fulltime - time; // Оставшееся время в мс

                u.sendProgress(sent, size, speed, rest);
            }
            u.sendDone(size);
        } catch (Throwable ex) {
            Logger.getLogger(DirectTransfer.class.getName()).log(Level.SEVERE, null, ex);
            u.sendFailed(ex);
        }
    }

    public static void receiveFile(String filePath, InputStream is, UIProxy u)
    {
        try {
            DataInputStream dis = new DataInputStream(is);
            String fileName = dis.readUTF();
            long fileLength = dis.readLong();
            long fileOffset = dis.readLong();

            File file = new File(filePath.concat(fileName));
            if (file.exists() && fileOffset == 0) {
                file.delete();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            int buffLen = 0;
            byte[] buffer;
            long startTime = System.currentTimeMillis();
            long speed = 0, fulltime = 0, time = 0, rest = 0;
            for (long c = fileOffset; c < fileLength; c += buffLen) {
                buffer = new byte[1024*10];
                buffLen = dis.read(buffer);
                if (buffLen < 0) {
                    break;
                }
                raf.write(buffer, 0, buffLen);
                
                // Цифры
                time = System.currentTimeMillis() - startTime; // Время в мс

                if (time==0) speed=0;
                else speed = 1000 * (c - fileOffset) / time; // байты в секунду

                if (c - fileOffset == 0)
                {
                    fulltime = rest = 0;
                }
                else
                {
                    fulltime = time * (fileLength - fileOffset) / (c - fileOffset); // Полное время в мс
                    rest = fulltime - time; // Оставшееся время в мс
                }

                u.receiveProgress(c,fileLength,speed,rest);
            }
            raf.close();
            u.receiveDone(fileLength);
        } catch (IOException ex) {
            Logger.getLogger(DirectTransfer.class.getName()).log(Level.SEVERE, null, ex);
            u.receiveFailed(ex);
        }
    }
}

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
            long speed = 0;
            byte[] b = new byte[1024 * 128];
            while (fis.available() > 0) {
                if (fis.available() < b.length) {
                    b = new byte[fis.available()];
                }
                fis.read(b);
                dos.write(b);
                dos.flush();
                sent += b.length;
                speed = System.currentTimeMillis() - starttime; // Время в мс
                speed /= 1000; // Время в секундах
                if (speed != 0) {
                    speed = sent / speed;
                } else {
                    speed = 0; // Скорость в байт/сек
                }
                speed/=1024; // Скорость в Кб/сек
                u.sendProgress(sent,size, (int) speed);
            }
            u.sendDone();
        } catch (Throwable ex) {
            Logger.getLogger(DirectTransfer.class.getName()).log(Level.SEVERE, null, ex);
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
            int speed = 0;
            for (long c = fileOffset; c < fileLength; c += buffLen) {
                buffer = new byte[1024*10];
                buffLen = dis.read(buffer);
                if (buffLen < 0) {
                    break;
                }
                raf.write(buffer, 0, buffLen);
                if (startTime != System.currentTimeMillis()) speed = (int) (((c - fileOffset) * 1000 / (System.currentTimeMillis() - startTime)) / 1024);
                u.receiveProgress(c,fileLength,speed);
            }
            raf.close();
        } catch (IOException ex) {
            Logger.getLogger(DirectTransfer.class.getName()).log(Level.SEVERE, null, ex);
        }
        u.receiveDone();
    }
}

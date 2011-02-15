/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.*;
import javax.imageio.ImageIO;

/**
 *
 * @author m1kc
 */
public class ScreenStreaming
{
    boolean streaming = false;

    boolean receiving = false;
    BufferedImage inputBuf = null;

    public void startStreaming(final OutputStream os)
    {
        new Thread(){
            @Override
            public void run()
            {
                final int w = Toolkit.getDefaultToolkit().getScreenSize().width;
                final int h = Toolkit.getDefaultToolkit().getScreenSize().height;
                final int wx = w/4;
                final int wy = w/4;
                DataOutputStream dos = new DataOutputStream(os);
                Robot robot = null;
                try {
                     robot = new Robot();
                } catch (AWTException ex) {
                    Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                }
                streaming = true;

                while(streaming)
                {
                    /*
                    try {
                        ImageIO.write(robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize())), "GIF", os);
                    } catch (IOException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     *
                     */

                    for (int i=0; i<w; i+=wx)
                    {
                        for (int j=0; j<h; j+=wy)
                        {
                            try {
                                dos.writeInt(w);
                                dos.writeInt(h);
                                dos.writeInt(i);
                                dos.writeInt(j);
                                ImageIO.write(robot.createScreenCapture(new Rectangle(i,j,wx,wy)), "GIF", os);
                            } catch (IOException ex) {
                                Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        }.start();
    }

    public void startReceiving(final InputStream is, final UIProxy u)
    {
        new Thread(){
            @Override
            public void run()
            {
                DataInputStream dis = new DataInputStream(is);
                receiving = true;

                while(receiving)
                {
                    try {
                        if (is.available() > 0)
                        {
                            int w = dis.readInt();
                            int h = dis.readInt();
                            int i = dis.readInt();
                            int j = dis.readInt();
                            //while (is.available()==0) Thread.sleep(10);
                            BufferedImage img = ImageIO.read(is);
                            is.read();
                            //System.out.println(is.available());
                            //System.out.println((char)is.read());
                            //System.out.println((char)is.read());
                            //System.out.println((char)is.read());

                            if (inputBuf == null) inputBuf = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
                            inputBuf.createGraphics().drawImage(img, i, j, null);
                            u.screenUpdated(inputBuf);
                        }
                        else Thread.sleep(10);
                    } catch (IOException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
}

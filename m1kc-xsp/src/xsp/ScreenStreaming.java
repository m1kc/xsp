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
    BufferedImage[][] outBuf = null;

    boolean receiving = false;
    BufferedImage inputBuf = null;

    public void startStreaming(final OutputStream os)
    {
        new Thread(){
            @Override
            public void run()
            {
                ImageIO.setUseCache(false);
                final int w = Toolkit.getDefaultToolkit().getScreenSize().width;
                final int h = Toolkit.getDefaultToolkit().getScreenSize().height;
                final int div = 2;
                final int wx = w/div;
                final int wy = h/div;
                DataOutputStream dos = new DataOutputStream(os);
                Robot robot = null;
                try {
                     robot = new Robot();
                } catch (AWTException ex) {
                    Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                }
                outBuf = new BufferedImage[div][div];//robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                for (int i=0; i<w; i+=wx)
                {
                    for (int j=0; j<h; j+=wy)
                    {
                        outBuf[i/wx][j/wy] = robot.createScreenCapture(new Rectangle(i,j,wx,wy));
                    }
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

                    long time = System.currentTimeMillis();

                    for (int i=0; i<w; i+=wx)
                    {
                        for (int j=0; j<h; j+=wy)
                        {
                            try {
                                BufferedImage img = robot.createScreenCapture(new Rectangle(i,j,wx,wy));
                                BufferedImage comp = outBuf[i/wx][j/wy];
                                boolean flag = true;
                                for (int ii = 0; ii<img.getWidth(); ii++)
                                    for (int jj = 0; jj<img.getHeight(); jj++)
                                        if (img.getRGB(ii, jj) != comp.getRGB(ii, jj)) flag = false;
                                if (!flag)
                                {
                                    dos.writeInt(w);
                                    dos.writeInt(h);
                                    dos.writeInt(i);
                                    dos.writeInt(j);
                                    ImageIO.write(img, "GIF", os);
                                    outBuf[i/wx][j/wy] = img;
                                    //System.out.println("Tansmitted: "+i+","+j);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    System.out.println("full transfer: "+(System.currentTimeMillis()-time));
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
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
                ImageIO.setUseCache(false);
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
                        else Thread.sleep(1);
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

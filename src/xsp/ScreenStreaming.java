package xsp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.logging.*;
import javax.imageio.ImageIO;

/**
 * @author m1kc
 */
public class ScreenStreaming {

    private boolean streaming = false;
    private BufferedImage[][] outBuf = null;

    private boolean receiving = false;
    private BufferedImage inputBuf = null;

    public void startStreaming(final OutputStream os) {
        new Thread() {
            @Override
            public void run() {
                ImageIO.setUseCache(false);
                final int w = Toolkit.getDefaultToolkit().getScreenSize().width;
                final int h = Toolkit.getDefaultToolkit().getScreenSize().height;
                final int div = 2;
                final int wx = w / div;
                final int wy = h / div;
                DataOutputStream dos = new DataOutputStream(os);
                Robot robot = null;
                try {
                    robot = new Robot();
                } catch (AWTException ex) {
                    Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                }
                outBuf = new BufferedImage[div][div];//robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                for (int i = 0; i < div; i++) {
                    for (int j = 0; j < div; j++) {
                        outBuf[i][j] = robot.createScreenCapture(new Rectangle(i * wx, j * wy, wx, wy));
                    }
                }
                streaming = true;

                while (streaming) {
                    long time = System.currentTimeMillis();

                    for (int i = 0; i < div; i++) {
                        for (int j = 0; j < div; j++) {
                            try {
                                BufferedImage img = robot.createScreenCapture(new Rectangle(i * wx, j * wy, wx, wy));
                                BufferedImage comp = outBuf[i][j];
                                boolean flag = true;
                                for (int ii = 0; ii < img.getWidth(); ii++)
                                    for (int jj = 0; jj < img.getHeight(); jj++)
                                        if (img.getRGB(ii, jj) != comp.getRGB(ii, jj)) flag = false;
                                if (!flag) {
                                    dos.writeInt(w);
                                    dos.writeInt(h);
                                    dos.writeInt(i * wx);
                                    dos.writeInt(j * wy);
                                    ImageIO.write(img, "GIF", os);
                                    outBuf[i][j] = img;
                                    //System.out.println("Tansmitted: "+i+","+j);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    System.out.println("full transfer: " + (System.currentTimeMillis() - time));
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }

    public void startReceiving(final InputStream is, final UIProxy u) {
        new Thread() {
            @Override
            public void run() {
                ImageIO.setUseCache(false);
                DataInputStream dis = new DataInputStream(is);
                receiving = true;

                while (receiving) {
                    try {
                        if (is.available() > 0) {
                            int w = dis.readInt();
                            int h = dis.readInt();
                            int i = dis.readInt();
                            int j = dis.readInt();

                            BufferedImage img = ImageIO.read(is);
                            is.read();

                            if (inputBuf == null) inputBuf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                            inputBuf.createGraphics().drawImage(img, i, j, null);
                            u.screenUpdated(inputBuf);
                        } else Thread.sleep(1);
                    } catch (IOException | InterruptedException ex) {
                        Logger.getLogger(ScreenStreaming.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }.start();
    }
}

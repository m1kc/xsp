/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xsp;

import java.io.*;
import java.net.*;
import javax.sound.sampled.*;
import javax.swing.*;

/**
 *
 * @author solkin
 */
public class NetSound {

    public JLabel inputSpeedLabel;
    public JLabel outputSpeedLabel;
    public JProgressBar melodyProgress;
    public int outputBufferSize = 256;//2048;
    public int inputBufferSize = 256;//2048;
    /** Microphone settings **/
    public float sampleRate = 4000;
    //8000,11025,16000,22050,44100
    public int sampleSizeInBits = 8;
    //8,16
    public int channels = 1;
    //1,2
    public boolean signed = true;
    //true,false
    public boolean bigEndian = false;
    //true,false
    public int frameSize = 8;
    public float frameRate = 4000;
    /** Connections **/
    private Socket socket;
    public OutputStream outputStream;
    public InputStream inputStream;
    // Выключатель
    public boolean mustStopOutput = false;
    public boolean mustStopInput = false;
    // Буфер
    public int buffer = -1;

    public void connectRemote(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        openStreams();
    }

    public void createServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        socket = serverSocket.accept();
        openStreams();
    }

    private void openStreams() throws IOException {
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
    }

    public boolean isConnected() {
        if (inputStream != null && outputStream != null) {
            return true;
        }
        return false;
    }

    public void closeConnection() throws IOException {
        outputStream.close();
        inputStream.close();
        socket.close();
    }

    public void broadcastUnformatData(AudioFormat audioFormat) throws IOException, LineUnavailableException {
        int num;
        byte[] data = new byte[outputBufferSize];
        long dataCounter = 0;

        if(!isConnected()) {
            return;
        }

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

        if (buffer <= 0) targetDataLine.open(audioFormat, dataLineInfo.getMinBufferSize());
        else targetDataLine.open(audioFormat, buffer);
        targetDataLine.start();

        long startTime = System.currentTimeMillis();
        while (true)
        {
            if (mustStopOutput) { mustStopOutput=false; break; }
            num = targetDataLine.read(data, 0, data.length);
            if (num==-1) break;
            outputStream.write(data, 0, num);
            outputStream.flush();
            dataCounter += num;
            if (System.currentTimeMillis() - startTime != 0) {
                outputSpeedLabel.setText(String.valueOf((int) (dataCounter / (System.currentTimeMillis() - startTime))));
            }
        }
        targetDataLine.drain();
        targetDataLine.close();
        targetDataLine = null;
        System.gc();
    }

    public void listenUnformatData(AudioFormat audioFormat) throws IOException, LineUnavailableException {
        int num;
        byte[] data = new byte[inputBufferSize];
        long dataCounter = 0;

        if(!isConnected()) {
            return;
        }

        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);

        if (buffer<=0) sourceDataLine.open(audioFormat, dataLineInfo.getMinBufferSize());
        else sourceDataLine.open(audioFormat, 1000);
        sourceDataLine.start();

        long startTime = System.currentTimeMillis();
        while (true)
        {
            num = inputStream.read(data);
            if (num==-1) break;
            if (mustStopInput)
            {
                while(inputStream.available()>0) inputStream.read();
                mustStopInput=false;
                break;
            }
            sourceDataLine.write(data, 0, num);
            dataCounter += num;
            if (System.currentTimeMillis() - startTime != 0) {
                inputSpeedLabel.setText(String.valueOf((int) (dataCounter / (System.currentTimeMillis() - startTime))));
            }
        }
        sourceDataLine.drain();
        sourceDataLine.close();
        sourceDataLine = null;
        System.gc();
    }

    public void broadcastFormattedSound(String fileName) throws FileNotFoundException, IOException {
        File file = new File(fileName);
        long fileSize = file.length();
        FileInputStream fis = new FileInputStream(file);
        if(!isConnected()) {
            return;
        }
        int num;
        byte[] data = new byte[outputBufferSize];
        long dataCounter = 0;
        long startTime = System.currentTimeMillis();
        while ((num = fis.read(data)) != -1) {
            outputStream.write(data, 0, num);
            dataCounter += num;
            if (System.currentTimeMillis() - startTime != 0) {
                outputSpeedLabel.setText(String.valueOf((int) (dataCounter / (System.currentTimeMillis() - startTime))));
                melodyProgress.setValue((int) (melodyProgress.getMaximum() * dataCounter / fileSize));
            }
        }
        melodyProgress.setValue(melodyProgress.getMaximum());
        fis.close();
    }

    public void listenFormattedSound() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if(!isConnected()) {
            return;
        }
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        AudioInputStream ais = AudioSystem.getAudioInputStream(bis);

        SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(ais.getFormat());
        sourceDataLine.open(ais.getFormat());
        sourceDataLine.start();
        int num;
        byte[] data = new byte[inputBufferSize];
        long dataCounter = 0;
        long startTime = System.currentTimeMillis();
        while ((num = ais.read(data)) != -1) {
            sourceDataLine.write(data, 0, num);
            dataCounter += num;
            if (System.currentTimeMillis() - startTime != 0) {
                inputSpeedLabel.setText(String.valueOf((int) (dataCounter / (System.currentTimeMillis() - startTime))));
            }
        }
        sourceDataLine.drain();
        ais.close();
    }

    public AudioFormat getDefaultAudioFormat() {
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}

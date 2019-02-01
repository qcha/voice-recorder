import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;

public class VoiceRecorder extends Thread {
    private TargetDataLine m_line;
    private AudioFileFormat.Type m_targetType;
    private AudioInputStream m_audioInputStream;
    private File m_outputFile;

    public VoiceRecorder(TargetDataLine m_line, Type m_targetType, File m_outputFile) {
        this.m_line = m_line;
        this.m_targetType = m_targetType;
        this.m_audioInputStream = new AudioInputStream(m_line);
        this.m_outputFile = m_outputFile;
    }

    public static void main(String args[]) {
        int frameSize;
        double time = 5; // seconds
        float frequency = 44100.0F;
        String dirName = "dir"; // todo change name

        if (new File(dirName).mkdir()) { // todo реакция на существующую папку
            //todo
        }

        AudioFormat audioFormat = new AudioFormat(frequency, 16, 2, true, false);
        frameSize = audioFormat.getFrameSize();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;
        TargetDataLine targetDataLine = null;
        VoiceRecorder recorder;

        File outputFile = new File(String.format("%s/audiorec.wav", dirName)); // todo change name
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            // todo change error
            System.out.println("unable to get a recording line");
            e.printStackTrace();
            System.exit(1);
        }

        recorder = new VoiceRecorder(targetDataLine, targetType, outputFile);
        recorder.start();

        System.out.println("Recording started.");
        try {
            Thread.currentThread().sleep(17000);
        } catch (InterruptedException e) {
            // todo change error reaction
            e.printStackTrace();
        }
        recorder.stopRecording();
        System.out.println("Recording stopped.");

        try {
            byte[] buf = new byte[(int) (time * frequency * frameSize)]; // the product is count of bytes in *time* seconds
            int bytes, i = 0;
            AudioInputStream mainFile = AudioSystem.getAudioInputStream(outputFile);

            while ((bytes = mainFile.read(buf)) > 0) {
                System.out.println(bytes);
                AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(buf), audioFormat, bytes / frameSize),
                        targetType, new File(String.format("%s/rec%d.wav", dirName, i))); // todo change name
                i++;
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            // todo change error reaction
            e.printStackTrace();
        }
    }

    public void start() {
        m_line.start();
        super.start();
    }

    public void stopRecording() {
        m_line.stop();
        m_line.close();
    }


    public void run() {
        try {
            AudioSystem.write(
                    m_audioInputStream,
                    m_targetType,
                    m_outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
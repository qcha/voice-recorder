package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import javax.sound.sampled.*;
import javax.sound.sampled.AudioFileFormat.Type;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Slf4j
public final class VoiceRecorder extends Thread {
    private TargetDataLine m_line;
    private AudioFileFormat.Type m_targetType;
    private AudioInputStream m_audioInputStream;
    private File m_outputFile;

    private File outputFile;
    private int frameSize;
    private double time = 5; // seconds
    private float frequency = 44100.0F;
    private String dirName = "dir"; // todo change name
    private TargetDataLine targetDataLine = null;

    private AudioFormat audioFormat;
    private AudioFileFormat.Type targetType;

    private Thread captureAudioThread;
    private volatile boolean isWorking;

    public VoiceRecorder(String dirName) {
        this.dirName = dirName;
    }

    private VoiceRecorder(TargetDataLine m_line, Type m_targetType, File m_outputFile) {
        this.m_line = m_line;
        this.m_targetType = m_targetType;
        this.m_audioInputStream = new AudioInputStream(m_line);
        this.m_outputFile = m_outputFile;
    }

    public void createAudioFile() {
        isWorking = true;

        if (new File(dirName).mkdir()) { // todo реакция на существующую папку
            //todo
        }

        audioFormat = new AudioFormat(frequency, 16, 2, true, false);
        frameSize = audioFormat.getFrameSize();
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
        targetType = AudioFileFormat.Type.WAVE;

        outputFile = new File(String.format("%s/audiorec.wav", dirName)); // todo change name
        try {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            // todo change error
            log.error("unable to get a recording line");
            e.printStackTrace();
            System.exit(1);
        }

        captureAudioThread = new Thread(new CaptureAudioThread());
        captureAudioThread.start();
    }

    public void divideAudioFile() {
        isWorking = false;
        try {
            captureAudioThread.join();
        } catch (InterruptedException e) {
            // todo change error
            e.printStackTrace();
        }

        try {
            if (outputFile.exists()) {
                byte[] buf = new byte[(int) (time * frequency * frameSize)]; // the product is count of bytes in *time* seconds
                int bytes, i = 0;
                AudioInputStream mainFile = AudioSystem.getAudioInputStream(outputFile);

                while ((bytes = mainFile.read(buf)) > 0) {
                    log.info(String.valueOf(bytes));
                    AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(buf), audioFormat, bytes / frameSize),
                            targetType, new File(String.format("%s/rec%d.wav", dirName, i))); // todo change name
                    i++;
                }
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

    private void stopRecording() {
        m_line.stop();
        m_line.close();
    }

    class CaptureAudioThread extends Thread {
        @Override
        public void run() {
            VoiceRecorder recorder = new VoiceRecorder(targetDataLine, targetType, outputFile);
            recorder.start();
            log.debug("Recording started.");
            StopWatch stopWatch = StopWatch.createStarted();
            while (stopWatch.getTime() < 600000 && isWorking) ;
            recorder.stopRecording();
            log.debug("Recording stopped.");
        }
    }
}
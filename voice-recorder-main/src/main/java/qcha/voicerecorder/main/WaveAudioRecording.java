package qcha.voicerecorder.main;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static qcha.voicerecorder.main.Constants.FREQUENCY;

public class WaveAudioRecording extends Thread implements AutoCloseable {
    private TargetDataLine dataLine;
    private AudioInputStream audioStream;
    private AudioFileFormat.Type type = AudioFileFormat.Type.WAVE;

    public WaveAudioRecording(String file) throws LineUnavailableException {
        AudioFormat audioFormat = new AudioFormat(FREQUENCY, 16, 2, true, false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

        dataLine = (TargetDataLine) AudioSystem.getLine(info);
        dataLine.open(audioFormat);

        audioStream = new AudioInputStream(dataLine);
    }

    @Override
    public void run() {
        dataLine.start();

        try {
            AudioSystem.write(
                    audioStream,
                    type,
                    new File("audiorec.wav"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        dataLine.stop();
        dataLine.close();
    }
}

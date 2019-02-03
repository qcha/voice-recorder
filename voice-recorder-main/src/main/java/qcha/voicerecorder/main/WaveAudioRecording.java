package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

import static qcha.voicerecorder.main.Constants.*;

@Slf4j
public class WaveAudioRecording extends Thread implements AutoCloseable {
    private TargetDataLine dataLine;
    private AudioInputStream audioStream;
    private File file;


    public WaveAudioRecording(String dir) throws LineUnavailableException {
        this.file = new File(dir, "all.wav");
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, AUDIO_FORMAT);

        dataLine = (TargetDataLine) AudioSystem.getLine(info);
        dataLine.open(AUDIO_FORMAT);
    }

    @Override
    public void run() {
        log.debug("Start recording.");
        dataLine.start();
        audioStream = new AudioInputStream(dataLine);

        try {
            AudioSystem.write(audioStream, AUDIO_TYPE, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        log.debug("Stop recording.");
        dataLine.stop();
        dataLine.close();
    }
}

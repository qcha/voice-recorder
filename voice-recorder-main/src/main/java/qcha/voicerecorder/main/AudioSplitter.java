package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static qcha.voicerecorder.main.Constants.*;

@Slf4j
public class AudioSplitter {
    private int frameSize;

    public AudioSplitter() {
        frameSize = AUDIO_FORMAT.getFrameSize();
    }

    public void split(int duration) {
        try {
            if (OUTPUT_FILE.exists()) {
                log.debug("Start splitting.");
                byte[] buf = new byte[(int) (duration * FREQUENCY * frameSize)]; // the product is count of bytes in *time* seconds
                int bytes, i = 0;
                AudioInputStream mainFile = AudioSystem.getAudioInputStream(OUTPUT_FILE);
                AudioInputStream ais;

                while ((bytes = mainFile.read(buf)) > 0) {
                    log.info(String.valueOf(bytes));

                    ais = new AudioInputStream(new ByteArrayInputStream(buf), AUDIO_FORMAT, bytes / frameSize);
                    AudioSystem.write(ais, AUDIO_TYPE, new File(String.format("%s/rec%d.wav", DIR_NAME, i))); // todo change name
                    i++;
                }
                log.debug("Splitting is ended.");
            }
        } catch (UnsupportedAudioFileException | IOException e) {
            // todo change error reaction
            e.printStackTrace();
        }
    }
}
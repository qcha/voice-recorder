package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

import static qcha.voicerecorder.main.Constants.*;

@Slf4j
public class AudioSplitter {
    private int frameSize;
    private int attempt;
    private File storageDir;

    public AudioSplitter(int attempt, String dir) {
        frameSize = AUDIO_FORMAT.getFrameSize();
        this.attempt = attempt;
        storageDir = new File(dir);
    }

    public void split(int duration) {
        try {
            if (storageDir.exists()) {
                File tmpFile = new File(storageDir, Constants.TMP_FILE_NAME);
                log.debug("Start splitting.");
                byte[] buf = new byte[(int) (duration * FREQUENCY * frameSize)]; // the product is count of bytes in *time* seconds
                int bytes, i = 0;
                AudioInputStream mainFile = AudioSystem.getAudioInputStream(tmpFile);
                AudioInputStream ais;

                while ((bytes = mainFile.read(buf)) > 0) {
                    log.info(String.valueOf(bytes));

                    ais = new AudioInputStream(new ByteArrayInputStream(buf), AUDIO_FORMAT, bytes / frameSize);
                    AudioSystem.write(ais, AUDIO_TYPE, new File(storageDir, String.format("%d0%d.wav", attempt, i)));
                    i++;
                }
                log.debug("Splitting is ended.");
                tmpFile.delete();
            }
        } catch (UnsupportedAudioFileException e) {
            log.error("Аудио-файл не поддерживается.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Ошибка при работе с файлом.");
            throw new UncheckedIOException(e);
        }
    }
}

package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;

import static qcha.voicerecorder.main.Constants.DURATION;

@Slf4j
public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private File allAudio;
    private int attempt;

    public AudioController(int attempt, String dir) throws LineUnavailableException {
        allAudio = new File(dir, Constants.TMP_FILE_NAME_TEMPLATE + "_" + attempt + ".wav");
        waveAudioRecording = new WaveAudioRecording(allAudio);
        this.attempt = attempt;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                waveAudioRecording.close();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }));
    }

    public void startRecord() {
        waveAudioRecording.start();
    }

    public void stopRecord() {
        try(AudioSplitter splitter = new AudioSplitter(attempt, allAudio))  {
            waveAudioRecording.close();
            splitter.split(DURATION);
        } catch (Exception e) {
            log.error("Ошибка при закрытии потока записи.");
            throw new RuntimeException(e);
        }

        allAudio.delete();
    }
}

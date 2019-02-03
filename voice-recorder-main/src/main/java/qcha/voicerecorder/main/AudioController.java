package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;

import static qcha.voicerecorder.main.Constants.DURATION;

@Slf4j
public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private AudioSplitter audioSplitter;
    private File allAudio;

    public AudioController(int attempt, String dir) throws LineUnavailableException {
        allAudio = new File(dir, Constants.TMP_FILE_NAME);
        waveAudioRecording = new WaveAudioRecording(allAudio);
        audioSplitter = new AudioSplitter(attempt, allAudio);


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
        try {
            waveAudioRecording.close();
            audioSplitter.split(DURATION);
            allAudio.delete();
        } catch (Exception e) {
            log.error("Ошибка при закрытии потока записи.");
            throw new RuntimeException(e);
        }
    }
}

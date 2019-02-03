package qcha.voicerecorder.main;

import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.LineUnavailableException;

import static qcha.voicerecorder.main.Constants.DURATION;
@Slf4j
public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private AudioSplitter audioSplitter;

    public AudioController(int attempt, String dir) throws LineUnavailableException {
        waveAudioRecording = new WaveAudioRecording(dir);
        audioSplitter = new AudioSplitter(attempt, dir);
    }

    public void startRecord() {
        waveAudioRecording.start();
    }

    public void stopRecord() {
        try {
            waveAudioRecording.close();
            audioSplitter.split(DURATION);
        } catch (Exception e) {
            log.error("Ошибка при закрытии потока записи.");
            throw new RuntimeException(e);
        }
    }
}

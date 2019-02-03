package qcha.voicerecorder.main;

import org.apache.commons.lang3.time.StopWatch;

import javax.sound.sampled.LineUnavailableException;

import static qcha.voicerecorder.main.Constants.DURATION;
import static qcha.voicerecorder.main.Constants.TIMER;

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
            throw new RuntimeException(e);
        }
        //todo call SuggestionWindow
    }
}

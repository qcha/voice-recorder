package qcha.voicerecorder.main;

import org.apache.commons.lang3.time.StopWatch;

import javax.sound.sampled.LineUnavailableException;

import static qcha.voicerecorder.main.Constants.TIMER;

public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private AudioSplitter audioSplitter;
    private volatile boolean isWorking = true;

    public AudioController(int attempt, String dir) throws LineUnavailableException {
        waveAudioRecording = new WaveAudioRecording(dir);
        audioSplitter = new AudioSplitter(attempt, dir);
    }

    public void startRecord() {
        startStopWatch();
        waveAudioRecording.start();
    }

    public void stopRecord() {
        isWorking = false;
        try {
            waveAudioRecording.close();
            audioSplitter.split(5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //todo call SuggestionWindow
    }

    private void startStopWatch() {
        Thread stopTimer = new Thread(() -> {
            StopWatch stopWatch = StopWatch.createStarted();
            while (stopWatch.getTime() < TIMER && isWorking) ;
            if (isWorking) {
                stopRecord(); // in case stopwatch end
            }
        });
        stopTimer.start();
    }

}

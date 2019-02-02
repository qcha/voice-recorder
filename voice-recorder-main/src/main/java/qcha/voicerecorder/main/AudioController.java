package qcha.voicerecorder.main;

import org.apache.commons.lang3.time.StopWatch;

import javax.sound.sampled.LineUnavailableException;

import static qcha.voicerecorder.main.Constants.TIMER;

public class AudioController {
    private WaveAudioRecording waveAudioRecording;
    private AudioSplitter audioSplitter;
    private volatile boolean isWorking = true;

    public AudioController() {
        try {
            waveAudioRecording = new WaveAudioRecording();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        audioSplitter = new AudioSplitter();
    }

    public void startRecord() {
        Thread recorder = new Thread(() -> waveAudioRecording.start());
        startStopWatch();
        recorder.start();
    }

    public void stopRecord() {
        isWorking = false;
        try {
            waveAudioRecording.close();
            divideAudio();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //todo call SuggestionWindow
    }

    private void divideAudio() {
        audioSplitter.split(5);
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

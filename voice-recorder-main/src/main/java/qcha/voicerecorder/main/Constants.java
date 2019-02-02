package qcha.voicerecorder.main;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import java.io.File;

public final class Constants {
    private Constants() {

    }

    public final static float FREQUENCY = 44100.0F;
    public final static String DIR_NAME;

    static {
        DIR_NAME = "direction"; // todo DIR_NAME initialization with EnterDirWindow
        new File(DIR_NAME).mkdir();
    }

    public final static AudioFormat AUDIO_FORMAT = new AudioFormat(FREQUENCY, 16, 2, true, false);
    public final static File OUTPUT_FILE = new File(String.format("%s/audiorec.wav", DIR_NAME));
    public final static long TIMER = 600000;
    public final static AudioFileFormat.Type AUDIO_TYPE = AudioFileFormat.Type.WAVE;


}

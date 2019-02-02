package qcha.voicerecorder.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import qcha.voicerecorder.main.AudioController;

import javax.sound.sampled.LineUnavailableException;

public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;

    private double length = 200;

    public VoiceRecorderView() {
        initButtons();

        setCenter(new HBox(recordBtn, stopBtn));
    }

    private void initButtons() {
        AudioController controller = new AudioController();

        recordBtn = new Button() {
            {
                setPrefSize(length, length);
                setText("Rec");
                setOnMouseClicked(e -> {
                    setDisable(true);
                    stopBtn.setDisable(false);
                    controller.startRecord();
                });
            }
        };

        stopBtn = new Button() {
            {
                setPrefSize(length, length);
                setText("Stop");
                setDisable(true);
                setOnMouseClicked(e -> {
                    setDisable(true);
                    recordBtn.setDisable(false);
                    controller.stopRecord();
                });
            }
        };
    }


}

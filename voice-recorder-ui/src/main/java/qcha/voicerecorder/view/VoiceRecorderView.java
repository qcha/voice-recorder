package qcha.voicerecorder.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import qcha.voicerecorder.main.AudioController;

import javax.sound.sampled.LineUnavailableException;

public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;
    private AudioController controller;

    private double length = 200;

    public VoiceRecorderView() {
        try {
            controller = new AudioController();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка инициализации микрофона");

            // alert.setHeaderText("Results:");
            alert.setHeaderText("Невозможно начать запись. Возможно, у вас не настроен микрофон?");

            alert.showAndWait();
            System.exit(-1);
        }
        initButtons();

        setCenter(new HBox(recordBtn, stopBtn));
    }

    private void initButtons() {
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

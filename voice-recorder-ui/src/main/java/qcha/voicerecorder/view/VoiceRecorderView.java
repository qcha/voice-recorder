package qcha.voicerecorder.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.extern.slf4j.Slf4j;
import qcha.voicerecorder.main.AudioController;

import javax.sound.sampled.LineUnavailableException;

@Slf4j
public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;
    private AudioController controller;
    private Label recordingLabel;

    private double length = 200;

    public VoiceRecorderView() {
        try {
            controller = new AudioController();
        } catch (Exception e) {
            log.error("Error while initializing audio controller: {}", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка инициализации микрофона");

            alert.setHeaderText("Невозможно начать запись. Возможно, у вас не настроен микрофон?");

            alert.showAndWait();
            System.exit(-1);
        }
        initButtons();

        recordingLabel = new Label("Идет запись");
        recordingLabel.setVisible(false);

        setTop(recordingLabel);
        setCenter(new HBox(recordBtn, stopBtn));
    }

    private void initButtons() {
        Image start = new Image(getClass().getResourceAsStream("/start.png"));
        Image stop = new Image(getClass().getResourceAsStream("/stop.png"));

        recordBtn = new Button() {
            {
                setPrefSize(length, length);
                setGraphic(new ImageView(start));
                setText("Запись");
                setOnMouseClicked(e -> {
                    setDisable(true);
                    stopBtn.setDisable(false);
                    recordingLabel.setVisible(true);
                    controller.startRecord();
                });
            }
        };

        stopBtn = new Button() {
            {
                setPrefSize(length, length);
                setGraphic(new ImageView(stop));
                setText("Остановка");
                setDisable(true);
                setOnMouseClicked(e -> {
                    setDisable(true);
                    recordBtn.setDisable(false);
                    recordingLabel.setVisible(false);
                    controller.stopRecord();
                });
            }
        };
    }


}

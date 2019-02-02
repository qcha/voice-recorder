package qcha.voicerecorder.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import qcha.voicerecorder.main.AudioController;

import java.io.File;

@Slf4j
public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;
    private AudioController controller;
    private Label recordingLabel;
    private VoiceRecorderViewModel voiceRecorderViewModel;

    private double length = 200;

    public VoiceRecorderView(VoiceRecorderViewModel viewModel) {
        this.voiceRecorderViewModel = viewModel;

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
                setOnMouseClicked(event -> {
                    setDisable(true);
                    stopBtn.setDisable(false);
                    recordingLabel.setVisible(true);

                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File directory = directoryChooser.showDialog(voiceRecorderViewModel.getStage());

                    try {
                        controller = new AudioController(voiceRecorderViewModel.getAttempt(), directory.getAbsolutePath());
                    } catch (Exception ex) {
                        log.error("Error while initializing audio controller: {}", ex);

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Ошибка инициализации микрофона");

                        alert.setHeaderText("Невозможно начать запись. Возможно, у вас не настроен микрофон?");

                        alert.showAndWait();
                        System.exit(-1);
                    }

                    controller.startRecord();
                    voiceRecorderViewModel.increaseAttempt();
                });
            }
        };

        stopBtn = new Button() {
            {
                setPrefSize(length, length);
                setGraphic(new ImageView(stop));
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

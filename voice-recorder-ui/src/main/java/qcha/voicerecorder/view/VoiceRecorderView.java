package qcha.voicerecorder.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import qcha.voicerecorder.main.AudioController;

import javax.sound.sampled.LineUnavailableException;
import java.io.File;

@Slf4j
public class VoiceRecorderView extends BorderPane {
    private Button recordBtn;
    private Button stopBtn;
    private AudioController controller;
    private Label recordingLabel;
    private VoiceRecorderViewModel voiceRecorderViewModel;

    private double length = 130;

    public VoiceRecorderView(VoiceRecorderViewModel viewModel) {
        this.voiceRecorderViewModel = viewModel;

        initButtons();

        recordingLabel = new Label("Идет запись...");
        recordingLabel.setFont(new Font("Cambria", 32));
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
                    DirectoryChooser directoryChooser = new DirectoryChooser();
                    File directory = directoryChooser.showDialog(voiceRecorderViewModel.getStage());

                    if (directory != null) {
                        setDisable(true);
                        stopBtn.setDisable(false);

                        try {
                            controller = new AudioController(voiceRecorderViewModel.getAttempt(),
                                    directory.getAbsolutePath());
                        } catch (LineUnavailableException ex) {
                            log.error("Error while initializing audio controller: {}", ex);

                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Ошибка инициализации микрофона");

                            alert.setHeaderText("Невозможно начать запись. Возможно, у вас не настроен микрофон?");

                            alert.showAndWait();
                            System.exit(-1);
                        }

                        recordingLabel.setVisible(true);
                        controller.startRecord();
                        voiceRecorderViewModel.increaseAttempt();
                    }
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

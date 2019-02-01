package qcha.voicerecorder.view;

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import qcha.voicerecorder.main.VoiceRecorder;

public class VoiceRecorderView extends BorderPane {
    private VoiceRecorder voiceRecorder;

    private Button recordBtn;
    private Button stopBtn;

    private double length = 200;

    public VoiceRecorderView() {
        EnterDirectoryWindow enterDirectoryWindow = new EnterDirectoryWindow();
        enterDirectoryWindow.showAndWait();

        voiceRecorder = new VoiceRecorder(enterDirectoryWindow.getDirName());

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
                    voiceRecorder.createAudioFile();
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
                    voiceRecorder.divideAudioFile();
                    //todo call SuggestionWindow
                });
            }
        };
    }


}

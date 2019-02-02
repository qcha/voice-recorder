package qcha.voicerecorder.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class MainApplication extends Application {
    private final double DEFAULT_WIDTH = 500;
    private final double DEFAULT_HEIGHT = 300;

    private Scene mainScene;
    private VoiceRecorderView voiceRecorderView;

    @Override
    public void start(Stage primaryStage) throws Exception {
        voiceRecorderView = new VoiceRecorderView();

        mainScene = new Scene(voiceRecorderView, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }
}

package qcha.voicerecorder.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class EnterDirectoryWindow {
    private Stage stage;
    private String dirName;

    public EnterDirectoryWindow() {
        stage = new Stage() {
            {
                setResizable(false);
                setTitle("Enter Directory name");
                initOwner(stage);
                setScene(new Scene(createEnterVinArea()));
            }
        };
    }

    public void showAndWait() {
        stage.showAndWait();
    }

    public String getDirName() {
        return dirName;
    }

    private HBox createEnterVinArea() {
        final Label label = new Label("Directory: ");
        final TextArea textArea = new TextArea();
        textArea.setPrefWidth(140);
        textArea.setMinHeight(25);
        textArea.setMaxHeight(25);
        final Button enterBtn = new Button("Enter");
        HBox hBox = new HBox() {
            {
                setPadding(new Insets(8));
                setSpacing(8);
            }
        };

        hBox.getChildren().addAll(label, textArea, enterBtn);

        enterBtn.setOnMouseClicked(e -> {
            String dirName = textArea.getText();
            if (dirName.length() > 0) {
                this.dirName = dirName;
                stage.close();
            }
        });

        return hBox;
    }
}

package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * 提示窗口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/25
 */
public class Prompt {

    public static void display(String title, String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(430);
        window.setMinHeight(300);

        Label promptLb = new Label();
        promptLb.setText(message);

        Button confirmBt = new Button("确认");
        confirmBt.setOnAction(event ->
                window.close()
        );

        VBox layout = new VBox(50);
        layout.getChildren().addAll(promptLb, confirmBt);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;


/**
 * 信息视图的控制器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class MessageController {
    @FXML
    private TextArea ta;

    @FXML
    private Button confirmBtn;

    public void setMassage(String message) {
        ta.setText(message);
    }

    public void setCloseAction(Stage stage) {
        confirmBtn.setOnAction(event -> {
            stage.close();
        });
    }
}

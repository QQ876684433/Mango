package ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * 主视图的控制器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/25
 */
public class MainController {
    @FXML
    private ChoiceBox methodBox;

    @FXML
    private Button sendButton;

    @FXML
    private TextField urlTf;

    @FXML
    private void initialize() {
        methodBox.setValue("GET");
    }

    @FXML
    private void sendRequest() {
        if (urlTf.getText().length() != 0)
            Prompt.display("已发送请求", "url: " + urlTf.getText());
        else
            Prompt.display("错误", "请填写url");
    }

}

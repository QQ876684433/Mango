package view;

import controller.MessageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * 用于展示文本的视图
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class MessageView {
    public static void display(String title, String message) {
        Stage stage = new Stage();
        stage.setTitle(title);
        FXMLLoader messageViewLoader = new FXMLLoader();
        messageViewLoader.setLocation(MessageView.class.getResource("/view/message_view.fxml"));
        try {
            Parent root = messageViewLoader.load();
            MessageController controller = messageViewLoader.getController();
            controller.setMassage(message);
            controller.setCloseAction(stage);
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.model.ParamTuple;

public class Main extends Application {

    private ObservableList<ParamTuple> initParamTuples = FXCollections.observableArrayList();

    public Main() {
        initParamTuples.add(new ParamTuple("key", "value", "description"));
    }

    public ObservableList<ParamTuple> getInitParamTuples() {
        return initParamTuples;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("/main.fxml"));

        Parent root = loader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 900, 550));

        MainController controller = loader.getController();
        controller.setMain(this);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

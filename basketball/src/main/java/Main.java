import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ParamTuple;

public class Main extends Application {

    private ObservableList<ParamTuple> initParamTuples = FXCollections.observableArrayList();

    public Main() {
        initParamTuples.add(new ParamTuple("key", "value", "description"));
    }

    public ObservableList<ParamTuple> getInitParamTuples() {
        ObservableList<ParamTuple> res = FXCollections.observableArrayList(initParamTuples);
        return res;
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(Main.class.getResource("view/request_view.fxml"));

        Parent root = mainLoader.load();
        primaryStage.setTitle("Request");
        primaryStage.setScene(new Scene(root, 900, 550));

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

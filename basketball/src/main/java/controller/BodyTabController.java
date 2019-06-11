package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DataStore;
import model.ParamTuple;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * body_tab视图的控制器类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/31
 */
public class BodyTabController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private RadioButton noneBtn;
    @FXML
    private RadioButton formDataBtn;
    @FXML
    private RadioButton urlEncodedBtn;
    @FXML
    private RadioButton rawBtn;
    @FXML
    private RadioButton binaryBtn;
    @FXML
    private ChoiceBox<String> rawType;
    @FXML
    private AnchorPane contentPane;
    @FXML
    private ToggleGroup tg1;
    @FXML
    private Button storeBtn;

    private int[] selectedBtn = DataStore.bodySelectedBtn;

    @FXML
    private void initialize() {
        noneBtn.setSelected(true);
        onSelectNone();
        storeBtn.setVisible(false);

        noneBtn.setOnAction(event -> {
            onSelectNone();
        });

        urlEncodedBtn.setOnAction(event -> {
            rawType.setVisible(false);
            initMapTable();
        });

        formDataBtn.setOnAction(event -> {
            rawType.setVisible(false);
            initMapTable();
        });

        rawBtn.setOnAction(event -> {
            rawType.setVisible(true);
            onSelectRaw();
        });

        binaryBtn.setOnAction(event -> {
            rawType.setVisible(false);
            onSelectBinary();
        });

        rawType.getSelectionModel().selectFirst();
        //设置按键的长度根据字长动态变换
        rawType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            double width = newValue.length() < 5 ? 65 : (newValue.length() < 30 ? newValue.length() * 10 : newValue.length() * 8);
            rawType.setPrefWidth(width);
        });

        storeBtn.setOnAction(event -> {
            Node node;
            switch (selectedBtn[0]) {
                case 2:
                    node = contentPane.getChildren().get(0);
                    DataStore.getFormData().removeIf(o -> true);
                    DataStore.getFormData().addAll(((TableView<ParamTuple>) node).getItems());
                    break;
                case 3:
                    node = contentPane.getChildren().get(0);
                    DataStore.getUrlEncoded().removeIf(o -> true);
                    DataStore.getUrlEncoded().addAll(((TableView<ParamTuple>) node).getItems());
                    break;
                case 4:
                    node = contentPane.getChildren().get(0);
                    DataStore.setRaw(((TextArea) node).getText());
                    DataStore.setRawType(rawType.getValue());
                    break;
                case 5:
                    node = contentPane.getChildren().get(1);
                    DataStore.setFilePath(((TextField) node).getText());
                    break;
                default:
                    break;
            }

        });


        tg1.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            switch (((RadioButton) tg1.getSelectedToggle()).getText()) {
                case "none":
                    selectedBtn[0] = 1;
                    break;
                case "form-data":
                    selectedBtn[0] = 2;
                    break;
                case "x-www-form-urlencoded":
                    selectedBtn[0] = 3;
                    break;
                case "raw":
                    selectedBtn[0] = 4;
                    break;
                case "binary":
                    selectedBtn[0] = 5;
                    break;
                default:
                    selectedBtn[0] = 0;
                    break;
            }

            storeBtn.setVisible(!((RadioButton) tg1.getSelectedToggle()).getText().equals("none"));
        });


    }


    private void onSelectNone() {
        removeContent();
        Label l = new Label();
        l.setText("This request does not have a body");
        l.setLayoutX(284);
        l.setLayoutY(70);
        updateContent(l);
    }

    private void initMapTable() {
        FXMLLoader mapLoader = new FXMLLoader();
        mapLoader.setLocation(this.getClass().getResource("/view/map_table.fxml"));
        try {
            Parent node = mapLoader.load();
            MapTableController controller = mapLoader.getController();
            controller.removeLabel();
            //移除label后所有组件上移
            for (Node n : node.getChildrenUnmodifiable()) {
                n.setLayoutY(n.getLayoutY() - 15);
            }

            ObservableList<ParamTuple> initItems = FXCollections.observableArrayList();
            initItems.add(new ParamTuple("key", "value", "description"));
            controller.setInitItems(initItems);
            updateContent(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onSelectRaw() {
        TextArea ta = new TextArea();
        ta.setLayoutX(1);
        ta.setLayoutY(30);
        ta.setPrefHeight(230);
        ta.setPrefWidth(816);
        updateContent(ta);

        updateContent(ta);
    }

    private void onSelectBinary() {
        removeContent();
        Button selectBtn = new Button();
        selectBtn.setLayoutX(48);
        selectBtn.setLayoutY(46);
        selectBtn.setPrefHeight(30);
        selectBtn.setPrefWidth(99);
        selectBtn.setText("select file");

        TextField fileNameField = new TextField();
        fileNameField.setLayoutX(176);
        fileNameField.setLayoutY(46);
        fileNameField.setPrefHeight(30);
        fileNameField.setPrefWidth(494);
        fileNameField.setText("");

        selectBtn.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(new Stage());
            if (file != null)
                fileNameField.setText(file.getPath());
        });

        updateContent(selectBtn, fileNameField);
    }

    private void updateContent(Node... node) {
        removeContent();
        addContent(node);
    }

    /**
     * 移除内容，为加入新内容作准备
     */
    private void removeContent() {
        contentPane.getChildren().removeIf(node -> true);
    }

    /**
     * 加入新内容
     *
     * @param node 即将加入的节点
     */
    private void addContent(Node... node) {
        ObservableList<Node> children = contentPane.getChildren();
        children.addAll(Arrays.asList(node));
    }


}

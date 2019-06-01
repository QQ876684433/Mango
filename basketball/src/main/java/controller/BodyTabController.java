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
import model.ParamTuple;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * body_tab的视图控制器
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
    private AnchorPane contentPane;

    @FXML
    private void initialize() {
        noneBtn.setSelected(true);
        onSelectNone();

        noneBtn.setOnAction(event -> {
            onSelectNone();
        });

        urlEncodedBtn.setOnAction(event -> {
            initMapTable();
        });

        formDataBtn.setOnAction(event -> {
            initMapTable();
        });

        rawBtn.setOnAction(event -> {
            onSelectRaw();
        });

        binaryBtn.setOnAction(event -> {
            onSelectBinary();
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
        ta.setLayoutY(39);
        ta.setPrefHeight(277);
        ta.setPrefWidth(816);
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

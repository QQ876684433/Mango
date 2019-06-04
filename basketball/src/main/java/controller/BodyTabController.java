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
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private void initialize() {
        noneBtn.setSelected(true);
        onSelectNone();

        noneBtn.setOnAction(event -> {
            rawType.setVisible(false);
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

    public String getContentType() {
        if (noneBtn.isSelected())
            return "text/plain";
        else if (formDataBtn.isSelected())
            return "multipart/form-data";
        else if (urlEncodedBtn.isSelected())
            return "application/x-www-form-urlencoded";
        else if (rawBtn.isSelected()) {
            if ("Text".equals(rawType.getValue())) {
                return "application/x-www-form-urlencoded";
            } else {
                String value = rawType.getValue();
                return value.substring(value.indexOf("(") + 1, value.indexOf(")"));
            }
        } else if (binaryBtn.isSelected()) {
            return "text/plain";
        }
        return "";

    }

    public byte[] getContent() {
        if (noneBtn.isSelected()) {
            return new byte[0];
        } else if (formDataBtn.isSelected()) {
            //TODO 未实现同时上传文本与文件
            List<ParamTuple> formUnits = getMapTableContent();
            return paramTuple2Bytes(formUnits);
        } else if (urlEncodedBtn.isSelected()) {
            List<ParamTuple> rows = getMapTableContent();
            return paramTuple2Bytes(rows);
        } else if (rawBtn.isSelected()) {
            TextArea textArea = (TextArea) contentPane.getChildren().get(0);
            return textArea.getText().getBytes();
        } else if (binaryBtn.isSelected()) {
            TextField filePath = (TextField) contentPane.getChildren().get(1);
            byte[] buffer = null;

            try {
                File file = new File(filePath.getText());
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] content = new byte[1000];
                int len;
                while ((len = fis.read(content)) != -1) {
                    bos.write(content, 0, len);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer;

        }
        return new byte[0];
    }

    private List<ParamTuple> getMapTableContent() {
        AnchorPane mapTablePane = (AnchorPane) contentPane.getChildren().get(0);
        for (Node node : mapTablePane.getChildren()) {
            if (node instanceof TableView) {
                return ((TableView<ParamTuple>) node).getItems();
            }
        }
        return new ArrayList<>();
    }

    private byte[] paramTuple2Bytes(List<ParamTuple> list) {
        final byte[][] bytes = {new byte[0]};
        list.forEach(t -> {
            bytes[0] = ArrayUtils.addAll(bytes[0], (t.getKey() + "=" + t.getValue()).getBytes());
        });
        return bytes[0];
    }


}

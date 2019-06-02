package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import model.ParamTuple;

import static util.ParamTupleTableHelper.combineRowWithDeleteBtn;
import static util.ParamTupleTableHelper.initParameterTable;

/**
 * map_table视图的控制器类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/26
 */
public class MapTableController {
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label mapLabel;

    @FXML
    private TableView<ParamTuple> mapTable;

    @FXML
    private TableColumn<ParamTuple, String> keyColumn;

    @FXML
    private TableColumn<ParamTuple, String> valueColumn;

    @FXML
    private TableColumn<ParamTuple, String> descriptionColumn;

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;


    @FXML
    private void initialize() {
        initParameterTable(keyColumn, valueColumn, descriptionColumn);
        combineRowWithDeleteBtn(mapTable, deleteBtn);

    }

    @FXML
    private void onAdd() {
        mapTable.getItems().add(new ParamTuple("key", "value", "description"));
    }

    @FXML
    private void onDelete() {
        int selectedIndex = mapTable.getSelectionModel().getSelectedIndex();
        mapTable.getItems().remove(selectedIndex);
    }

    public void setLabelText(String text) {
        mapLabel.setText(text);
    }

    public void removeLabel() {
        rootPane.getChildren().removeIf(node -> node instanceof Label);
    }

    public void setInitItems(ObservableList<ParamTuple> items) {
        mapTable.setItems(FXCollections.observableArrayList(items));
    }


}

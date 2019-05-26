package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import ui.model.ParamTuple;
import ui.view.Prompt;

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
    private Button addButton;
    @FXML
    private Button deleteButton;

    @FXML
    private TextField urlTf;

    @FXML
    private TableView<ParamTuple> paramTable;

    @FXML
    private TableColumn<ParamTuple, String> keyColumn;

    @FXML
    private TableColumn<ParamTuple, String> valueColumn;

    @FXML
    private TableColumn<ParamTuple, String> descriptionColumn;

    @FXML
    private Tab paramTab;

    private Main main;

    @FXML
    private void initialize() {

        //为该列绑定model类的property，下同
        keyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        //将该列的单元设定为能即时编辑，下同
        keyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        keyColumn.setOnEditCommit((TableColumn.CellEditEvent<ParamTuple, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setKey(t.getNewValue());
        });

        valueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit((TableColumn.CellEditEvent<ParamTuple, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
        });
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setOnEditCommit((TableColumn.CellEditEvent<ParamTuple, String> t) -> {
            (t.getTableView().getItems().get(t.getTablePosition().getRow())).setDescription(t.getNewValue());
        });

        //将delete按钮与被选中的item(一行)联系起来，避免未选中行时出现delete按钮也可操作的异常
        paramTable.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            deleteButton.setDisable(newSelection == null);
        });

    }


    @FXML
    private void sendRequest() {
        if (urlTf.getText().length() != 0)
            Prompt.display("已发送请求", "url: " + urlTf.getText());
        else
            Prompt.display("错误", "请填写url");
    }

    @FXML
    private void onAdd() {
        paramTable.getItems().add(new ParamTuple("key", "value", "description"));
    }

    @FXML
    private void onDelete() {
        int selectedIndex = paramTable.getSelectionModel().getSelectedIndex();
        paramTable.getItems().remove(selectedIndex);
    }

    public void setMain(Main main) {
        this.main = main;
        paramTable.setItems(main.getInitParamTuples());

    }

}

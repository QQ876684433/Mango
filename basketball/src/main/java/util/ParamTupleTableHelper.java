package util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import model.ParamTuple;

import java.util.Arrays;

/**
 * 参数化table的辅助类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/1
 */
public class ParamTupleTableHelper {

    public static void initParameterTable(TableColumn<ParamTuple, String> keyColumn,
                                          TableColumn<ParamTuple, String> valueColumn,
                                          TableColumn<ParamTuple, String> descriptionColumn) {
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
    }

    public static void combineRowWithDeleteBtn(TableView<?> table, Button deleteBtn) {
        //将delete按钮与被选中的item(一行)联系起来，避免未选中行时出现delete按钮也可操作的异常
        table.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            deleteBtn.setDisable(newSelection == null);
        });
    }

    public static ObservableList<ParamTuple> packagePT(ParamTuple... items) {
        return FXCollections.observableArrayList(Arrays.asList(items));
    }
}

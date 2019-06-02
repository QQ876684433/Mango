package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.ParamTuple;
import util.ParamTupleTableHelper;


/**
 * 响应视图的控制器类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/2
 */
public class ResponseViewController {
    @FXML
    private TableView<ParamTuple> statusTable;

    @FXML
    private TableView<ParamTuple> headersTable;

    @FXML
    private TableColumn<ParamTuple, String> statusNameColumn;

    @FXML
    private TableColumn<ParamTuple, String> statusValueColumn;

    @FXML
    private TableColumn<ParamTuple, String> headerKeyColumn;

    @FXML
    private TableColumn<ParamTuple, String> headerValueColumn;

    @FXML
    private void initialize() {
        statusNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        statusValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        headerKeyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        headerValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

    }

    public void setStatus(ParamTuple... items) {
        statusTable.setItems(ParamTupleTableHelper.packagePT(items));
    }

    public void setHeaders(ParamTuple... items) {
        headersTable.setItems(ParamTupleTableHelper.packagePT(items));
    }

    public void setContent(String contentType, byte[] content) {
        //TODO 待实现，需考虑文本、文件、流媒体此三种情况
    }


}

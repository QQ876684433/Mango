package controller;

import http.core.HttpResponse;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import model.ParamTuple;


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
    private TextArea bodyArea;

    @FXML
    private TextArea plainArea;


    @FXML
    private void initialize() {
        statusNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        statusValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        headerKeyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        headerValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

    }

    public void bindResponse(HttpResponse response) {
        ParamTuple status = new ParamTuple("status", response.getStatus() + " : " + response.getMessage(), "");
        ParamTuple version = new ParamTuple("version", response.getVersion(), "");
        statusTable.getItems().addAll(status, version);

        response.getHeader().getHeaders().forEach((k, v) -> {
            ParamTuple header = new ParamTuple((String) k, (String) v, "");
            headersTable.getItems().add(header);
        });

        //TODO get response body
        bodyArea.setText(response.getResponseBodyText());

        plainArea.setText(response.toString());
    }


}

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import model.ParamTuple;
import view.Prompt;

import java.io.IOException;

import static util.ParamTupleTableHelper.*;

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
    private Button paramAddBtn;
    @FXML
    private Button paramDeleteBtn;

    @FXML
    private Button headerAddBtn;
    @FXML
    private Button headerDeleteBtn;

    @FXML
    private TextField urlTf;

    @FXML
    private TableView<ParamTuple> paramTable;

    @FXML
    private TableColumn<ParamTuple, String> paramKeyColumn;

    @FXML
    private TableColumn<ParamTuple, String> paramValueColumn;

    @FXML
    private TableColumn<ParamTuple, String> paramDescColumn;

    @FXML
    private TableView<ParamTuple> headerTable;

    @FXML
    private TableColumn<ParamTuple, String> headerKeyColumn;

    @FXML
    private TableColumn<ParamTuple, String> headerValueColumn;

    @FXML
    private TableColumn<ParamTuple, String> headerDescColumn;

    @FXML
    private Tab paramTab;

    @FXML
    private Tab headerTab;

    @FXML
    private Tab bodyTab;

//    private Main main;

    @FXML
    private void initialize() {

        //param和header页面逻辑实际完全一样，但还不知道怎样复用一个界面，就先这样

        initParameterTable(paramKeyColumn, paramValueColumn, paramDescColumn);
        combineRowWithDeleteBtn(paramTable, paramDeleteBtn);
        paramTable.setItems(packagePT(new ParamTuple("key", "value", "description")));

        initParameterTable(headerKeyColumn, headerValueColumn, headerDescColumn);
        combineRowWithDeleteBtn(headerTable, headerDeleteBtn);
        headerTable.setItems(packagePT(new ParamTuple("Content-Type", "application/x-www-form-urlencoded", "")));

        FXMLLoader bodyLoader = new FXMLLoader();
        bodyLoader.setLocation(this.getClass().getResource("/view/body_tab.fxml"));
        try {
            bodyTab.setContent(bodyLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void sendRequest() {
        if (urlTf.getText().length() != 0)
            Prompt.display("已发送请求", "url: " + urlTf.getText());
        else
            Prompt.display("错误", "请填写url");
    }

    @FXML
    private void onParamAdd() {
        paramTable.getItems().add(new ParamTuple("key", "value", "description"));
    }

    @FXML
    private void onHeaderAdd() {
        headerTable.getItems().add(new ParamTuple("key", "value", "description"));
    }

    @FXML
    private void onParamDelete() {
        int selectedIndex = paramTable.getSelectionModel().getSelectedIndex();
        paramTable.getItems().remove(selectedIndex);
    }

    @FXML
    private void onHeaderDelete() {
        int selectedIndex = headerTable.getSelectionModel().getSelectedIndex();
        headerTable.getItems().remove(selectedIndex);
    }


}

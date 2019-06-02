package controller;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpMethod;
import http.util.HttpVersion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ParamTuple;
import util.RequestHelper;
import util.SocketContext;
import view.Prompt;

import java.io.IOException;
import java.net.Socket;

import static util.ParamTupleTableHelper.*;

/**
 * 请求视图的控制器类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/5/25
 */
public class RequestViewController {
    @FXML
    private ChoiceBox<String> methodBox;

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

        methodBox.getSelectionModel().selectFirst();

    }


    @FXML
    private void sendRequest() {
        if (check()) {
//            Prompt.display("已发送请求", "url: " + urlTf.getText());
            HttpRequest request = buildHttpRequest();
            // 如果是第一次发送请求，建立socket链接
            if (!SocketContext.initialized()) {
                try {
                    //格式判断
                    String ip = getIp();
                    int port = getPort();
                    if (!(0 <= port && port <= 65535))
                        throw new RuntimeException("端口格式错误");
                    SocketContext.build(ip, port); //TODO 建立socket的链接
                } catch (NumberFormatException e) {
                    Prompt.display("异常", "端口格式错误");
                } catch (RuntimeException e) {
                    Prompt.display("异常", e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Prompt.display("异常", "socket链接建立失败");
                }
            }
            Socket s = SocketContext.getSocket();
            HttpResponse response;
            try {
                request.writeTo(s.getOutputStream());
                // 在此处阻塞
                response = new HttpResponse(s.getInputStream());
                Stage responseStage = new Stage();
                responseStage.setTitle("Response");
                FXMLLoader responseViewLoader = new FXMLLoader();
                responseViewLoader.setLocation(this.getClass().getResource("/view/response_view.fxml"));

                try {
                    Parent responseViewRoot = responseViewLoader.load();
                    ResponseViewController responseController = responseViewLoader.getController();
                    //TODO responseController.setData();
                    responseStage.setScene(new Scene(responseViewRoot, 850, 500));
                    responseStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Prompt.display("异常", "读取响应界面fxml文件时发生异常");
                }

            } catch (IOException e) {
                e.printStackTrace();
                Prompt.display("错误", "请求失败");
            }


        }
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

    /**
     * 根据使用者的设置构建{@link HttpRequest}实例
     *
     * @return 构造完毕的HttpRequest实例
     */
    private HttpRequest buildHttpRequest() {
        HttpRequest request = new HttpRequest();
        request.setVersion(HttpVersion.HTTP_VERSION_1_1);
        request.setMethod(methodBox.getValue());
        request.setUrl(
                urlTf.getText() + (methodBox.getValue().equals(HttpMethod.GET) ?
                        ("?" + RequestHelper.buildParamString(paramTable.getItems())) : ""));
        headerTable.getItems()
                .filtered(RequestHelper::validateHeader)
                .forEach(o -> {
                    request.setHeader(o.getKey(), o.getValue());
                });

        //TODO setBody

        return request;
    }

    private boolean check() {
        //TODO 在发送之前可能需要检查参数设置的正确性
        if (urlTf.getText().length() == 0) {
            Prompt.display("错误", "请填写url");
            return false;
        }
        return true;
    }

    private String getIp() {
        String url = urlTf.getText();
        return url.substring(0, url.indexOf(":"));
    }

    private int getPort() {
        String url = urlTf.getText();
        return Integer.parseInt(url.substring(url.indexOf(":") + 1, url.indexOf("/")));
    }


}

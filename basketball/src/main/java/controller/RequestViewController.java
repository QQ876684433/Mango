package controller;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.util.HttpMethod;
import http.util.HttpVersion;
import http.util.header.RequestHeader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ParamTuple;
import util.RequestHelper;
import util.SocketHolder;
import view.Prompt;

import java.io.ByteArrayInputStream;
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

        sendButton.setOnAction(event -> {
            try {
                sendRequest();
            } catch (Exception e) {
                e.printStackTrace();
                Prompt.display("异常", e.getMessage());
            }
        });

    }


    /**
     * 发送请求，得到响应，展示响应界面
     *
     * @throws Exception 各种过程中可能会产生的异常
     */
    private void sendRequest() throws Exception {
//            Prompt.display("已发送请求", "url: " + urlTf.getText());
        check();
        HttpRequest request = buildHttpRequest();
        Socket s;
        try {
            //格式判断
            String ip = getIp();
            int port = getPort();
            if (!(0 <= port && port <= 65535))
                throw new Exception("端口格式错误");

            s = SocketHolder.of(ip, port);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("端口格式错误");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("socket链接建立失败");
        }

        HttpResponse response;
        try {
            request.writeTo(s.getOutputStream());
//             在此处阻塞
            response = new HttpResponse(s.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("请求失败");
        }

        Stage responseStage = new Stage();
        responseStage.setTitle("Response");
        FXMLLoader responseViewLoader = new FXMLLoader();
        responseViewLoader.setLocation(this.getClass().getResource("/view/response_view.fxml"));

        try {
            Parent responseViewRoot = responseViewLoader.load();
            ResponseViewController responseController = responseViewLoader.getController();
            responseController.bindResponse(response);
            responseStage.setScene(new Scene(responseViewRoot, 850, 500));
            responseStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("读取响应界面fxml文件时发生异常");
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
    private HttpRequest buildHttpRequest() throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/view/body_tab.fxml"));
        Parent bodyTab = loader.load();
        BodyTabController bodyTabController = loader.getController();

        HttpRequest request = new HttpRequest();
        request.setVersion(HttpVersion.HTTP_VERSION_1_1);
        request.setMethod(methodBox.getValue());
        request.setUrl(
                getUrl() + (methodBox.getValue().equals(HttpMethod.GET) ?
                        ("?" + RequestHelper.buildParamString(paramTable.getItems())) : ""));

        //TODO set request body
        if (methodBox.getValue().equals(HttpMethod.POST)) {
            request.setHeader(RequestHeader.CONTENT_TYPE, bodyTabController.getContentType());

            byte[] content = bodyTabController.getContent();
            int contentLength = content.length;
            request.setRequestBody(new ByteArrayInputStream(content));
            request.setHeader(RequestHeader.CONTENT_LENGTH, String.valueOf(contentLength));
        }

        request.setHeader(RequestHeader.ACCEPT, "*/*");
        request.setHeader(RequestHeader.CONNECTION, "keep-alive; timeout=30,max=10");
        request.setHeader(RequestHeader.ACCEPT_ENCODING, "gzip, deflate");

        //手动设置的header可以覆盖系统行为
        headerTable.getItems()
                .filtered(RequestHelper::validateHeader)
                .forEach(o -> {
                    request.setHeader(o.getKey(), o.getValue());
                });


        return request;
    }

    private void check() {
        //TODO validate those parameters
        if (urlTf.getText().length() == 0) {
            throw new RuntimeException("请填写url");

        }
    }

    private String getUrl() {
        String raw = urlTf.getText();
        return raw.substring(raw.indexOf("/"));
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

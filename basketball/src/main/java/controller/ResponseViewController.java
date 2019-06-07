package controller;

import http.core.HttpResponse;
import http.util.header.ResponseHeader;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.ParamTuple;

import java.io.*;


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
    private AnchorPane bodyPane;

    @FXML
    private TextArea plainArea;


    @FXML
    private void initialize() {
        statusNameColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        statusValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        headerKeyColumn.setCellValueFactory(cellData -> cellData.getValue().keyProperty());
        headerValueColumn.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

    }

    public void bindResponse(String uri, HttpResponse response) {
        ParamTuple status = new ParamTuple("status", response.getStatus() + " : " + response.getMessage(), "");
        ParamTuple version = new ParamTuple("version", response.getVersion(), "");
        statusTable.getItems().addAll(status, version);

        response.getHeader().getHeaders().forEach((k, v) -> {
            ParamTuple header = new ParamTuple((String) k, (String) v, "");
            headersTable.getItems().add(header);
        });

        //TODO get response body
//        switch (response.getHeader().getProperty(ResponseHeader.CONTENT_TYPE)) {
//            case "text/plain":
//                bodyArea.setText(response.getResponseBodyText());
//                break;
//            case "image/png":
//                this.fileTransformer = new ImageTransformer();
//                break;
//            case "audio/mp3":
//                this.fileTransformer = new AudioTransformer();
//                break;
//            case "video/mpeg4":
//                this.fileTransformer = new VideoTransformer();
//                break;
//        }
        String contentType = response.getHeader().getProperty(ResponseHeader.CONTENT_TYPE);
        if (contentType.equals("image/png")) {
            setImageBody(response.getResponseBodyStream());
        } else {
            setTextBody(response.getResponseBodyText());
            if (contentType.equals("audio/mp3") || contentType.equals("video/mpeg4")) {
                InputStream in = response.getResponseBodyStream();

                DirectoryChooser chooser = new DirectoryChooser();
                File filePath = chooser.showDialog(new Stage());
                if (filePath != null) {
                    String path = filePath.getPath();
                    String postfix = contentType.equals("audio/mp3") ? ".mp3" : ".mp4";
                    File file = new File(path + "/" + uri + postfix);
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }

                    try {
                        file.createNewFile();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] b = new byte[1024];
                        while ((in.read(b)) != -1) {
                            fos.write(b);// 写入数据
                        }
                        fos.close();// 保存数据
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        bodyArea.setText(response.getResponseBodyText());

        plainArea.setText(response.toString());
    }

    public void setBody(InputStream in) {
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            while ((line = br.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bodyArea.setText(builder.toString());
    }

    public void setTextBody(String text) {
        bodyArea.setText(text);
    }

    public void setImageBody(InputStream in) {
        Image image = new Image(in);

        ImageView imageView = new ImageView();
        imageView.setImage(image);

        ObservableList<Node> children = bodyPane.getChildren();
        children.removeIf(o -> true);
        children.add(imageView);
    }


}

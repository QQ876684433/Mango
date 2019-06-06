import http.core.HttpRequest;
import http.core.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * simple introduction
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/6
 */
public class MockServer {
    private static final Integer port = 8080;//HTTP默认端口80

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            //建立服务器Socket,监听客户端请求
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port:" + serverSocket.getLocalPort());
            //死循环不间断监听客户端请求
            while (true) {
                final Socket socket = serverSocket.accept();
                System.out.println("build a new tcp link with client,the client address:" +
                        socket.getInetAddress() + ":" + socket.getPort());

                try {
                    InputStream in = socket.getInputStream();
                    HttpRequest request = new HttpRequest(in);
                    System.out.println("receive request from client: "
                            + socket.getPort()
                            + "\n" + "content: "
                            + request.toString());

                    HttpResponse response = new HttpResponse();
                    response.writeTo(socket.getOutputStream());
                    System.out.println("send response");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

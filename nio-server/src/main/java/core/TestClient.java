package core;

import java.io.*;
import java.net.Socket;

public class TestClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 8080);
            //向服务器端第一次发送字符串
            OutputStream netOut = socket.getOutputStream();
            DataOutputStream doc = new DataOutputStream(netOut);
            DataInputStream in = new DataInputStream(socket.getInputStream());

            //向服务器端第二次发送字符串
            doc.writeUTF("list");


            doc.writeUTF("bye");

            BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //由Socket对象得到输出流，并构造PrintWriter对象
            PrintWriter os=new PrintWriter(socket.getOutputStream());
            //由系统标准输入设备构造BufferedReader对象
            BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
            //在标准输出上打印从客户端读入的字符串
            System.out.println("Client:"+is.readLine());
            //从标准输入读入一字符串
            String line=sin.readLine();
            //如果该字符串为 "bye"，则停止循环
            while(!line.equals("bye")){
                //向客户端输出该字符串
                os.println(line);
                //刷新输出流，使Client马上收到该字符串
                os.flush();
                //在系统标准输出上打印读入的字符串
                System.out.println("Server:"+line);
                //从Client读入一字符串，并打印到标准输出上
                System.out.println("Client:"+is.readLine());
                //从系统标准输入读入一字符串
                line=sin.readLine();
            }



            doc.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();//出错，打印出错信息
        }
    }
}

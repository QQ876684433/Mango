package util;

import java.io.IOException;
import java.net.Socket;

/**
 * 保存socket实例的上下文类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2019/6/3
 */
public class SocketContext {
    //客户端全局唯一应使用的socket实例
    private static Socket socket = null;

    /**
     * 建立socket链接
     *
     * @param ip   目标ip
     * @param port 目标端口
     */
    public static void build(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
    }

    /**
     * 判断socket链接是否已经建立
     *
     * @return 如果已建立socket链接，返回是，否则返回否
     */
    public static boolean initialized() {
        return socket != null;
    }

    /**
     * 得到socket单例
     *
     * @return 返回socket单例，如果socket未初始化则为null
     */
    public static Socket getSocket() {
        return socket;
    }
}

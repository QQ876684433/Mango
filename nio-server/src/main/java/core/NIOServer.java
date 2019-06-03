package core;

import http.core.HttpRequest;
import http.core.HttpResponse;
import http.exception.HttpParseFailException;
import http.util.handler.HttpRequestUtils;
import http.util.handler.HttpUtils;
import service.HttpContext;
import service.HttpMethodFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        Selector serverSelector = Selector.open();
        Selector clientSelector = Selector.open();

        new Thread(() -> {
            try {
                // 对应IO编程中服务端启动
                ServerSocketChannel listenerChannel = ServerSocketChannel.open();
                listenerChannel.socket().bind(new InetSocketAddress(8080));
                listenerChannel.configureBlocking(false);
                listenerChannel.register(serverSelector, SelectionKey.OP_ACCEPT);

                while (true) {
                    // 监测是否有新的连接，这里的1指的是阻塞的时间为1ms
                    if (serverSelector.select(1) > 0) {
                        System.out.println("new connection");
                        Set<SelectionKey> set = serverSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();

                            if (key.isAcceptable()) {
                                try {
                                    // (1) 每来一个新连接，不需要创建一个线程，而是直接注册到clientSelector
                                    SocketChannel clientChannel = ((ServerSocketChannel) key.channel()).accept();
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(clientSelector, SelectionKey.OP_READ);
                                } finally {
                                    keyIterator.remove();
                                }
                            }

                        }
                    }
                }
            } catch (IOException ignored) {
            }

        }).start();


        new Thread(() -> {
            try {
                while (true) {
                    // (2) 批量轮询是否有哪些连接有数据可读，这里的1指的是阻塞的时间为1ms
                    if (clientSelector.select(1) > 0) {
                        Set<SelectionKey> set = clientSelector.selectedKeys();
                        Iterator<SelectionKey> keyIterator = set.iterator();

                        while (keyIterator.hasNext()) {
                            SelectionKey key = keyIterator.next();
                            SocketChannel clientChannel = (SocketChannel) key.channel();

                            if (key.isReadable()) {
                                try {
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                    // (3) 读取数据以块为单位批量读取
                                    clientChannel.read(byteBuffer);
                                    byteBuffer.flip();

                                    System.out.println(Charset.defaultCharset().newDecoder().decode(byteBuffer)
                                            .toString());
                                    // 拿到客户端发来的请求
                                    HttpRequest httpRequest = new HttpRequest(new ByteArrayInputStream(byteBuffer.array()));

                                    // TODO
                                    //  要根据请求报文来判断是长链接或短链接
                                    HttpUtils httpUtils = new HttpRequestUtils(httpRequest);
                                    if(httpUtils.isLongConnection()){
                                        Properties keepAlive = httpUtils.getLongConnectionDuration();
                                        String timout = keepAlive.getProperty(HttpUtils.KEEP_ALIVE_TIMEOUT);
                                        String max = keepAlive.getProperty(HttpUtils.KEEP_ALIVE_MAX);
                                    }
                                    //  如果是长链接且已经绑定了计时器或计数器，则判断是否继续处理
                                    Object attach = key.attachment();
                                    //  否则绑定一个计时器或者计数器
                                    key.attach(attach);

                                    // 处理请求
                                    HttpContext httpContext = new HttpContext(HttpMethodFactory.getHttpMethod(httpRequest));
                                    HttpResponse httpResponse = httpContext.processRequest(httpRequest);

                                    byteBuffer.clear();
                                    // 返回响应
                                    byteBuffer.put(httpResponse.toString().getBytes());
                                    clientChannel.write(byteBuffer);
                                } catch (HttpParseFailException ignored){

                                } catch (Exception e){
                                    e.printStackTrace();
                                } finally {
                                    keyIterator.remove();
                                    key.interestOps(SelectionKey.OP_READ);
                                    // TODO
                                    //  这里如果是短链接则直接关闭连接
                                    clientChannel.socket().close();
                                }
                            }
                        }
                    }
                }
            } catch (IOException ignored) {
            }
        }).start();


    }
}
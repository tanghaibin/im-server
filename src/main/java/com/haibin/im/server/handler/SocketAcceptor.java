package com.haibin.im.server.handler;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * 监听连接端口请求套接字
 *
 * @author haibin.tang
 * @create 2019-08-08 2:39 PM
 **/
public class SocketAcceptor {

    public void start() throws Exception {
        ReaderHandler readerHandler = new ReaderHandler();
        ServerSocket serverSocket = ServerSocketChannel.open().socket();
        serverSocket.bind(new InetSocketAddress(8888));
        while (true) {
            Socket socket = serverSocket.accept();
            socket.getChannel().configureBlocking(false);
            readerHandler.handle(socket.getChannel());
        }
    }
}

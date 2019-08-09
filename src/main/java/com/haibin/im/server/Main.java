package com.haibin.im.server;

import com.haibin.im.server.handler.SocketAcceptor;

/**
 * @author haibin.tang
 * @create 2019-08-08 3:35 PM
 **/
public class Main {

    public static void main(String[] args) throws Exception {
        new SocketAcceptor().start();
    }
}

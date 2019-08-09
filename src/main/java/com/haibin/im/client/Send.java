package com.haibin.im.client;

import com.haibin.im.server.protocol.Protocol;
import com.haibin.im.server.util.JsonUtil;

import java.io.IOException;
import java.net.Socket;

/**
 * @author haibin.tang
 * @create 2019-08-08 5:41 PM
 **/
public class Send {

    public static void main(String[] args) throws IOException {

        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = new Socket("localhost", 8888);
                    while (true) {
                        Protocol protocol = new Protocol();
                        protocol.setTo("1");
                        protocol.setFrom("10");
                        protocol.setBody("how are you?");

                        socket.getOutputStream().write(JsonUtil.obj2Json(protocol).getBytes());
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Socket socket = new Socket("localhost", 8888);
                    while (true) {
                        Protocol protocol = new Protocol();
                        protocol.setTo("10");
                        protocol.setFrom("1");
                        protocol.setBody("what are you doing?");

                        socket.getOutputStream().write(JsonUtil.obj2Json(protocol).getBytes());
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

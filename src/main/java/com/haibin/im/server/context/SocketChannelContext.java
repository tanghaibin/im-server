package com.haibin.im.server.context;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * 套接字通道相关
 *
 * @author haibin.tang
 * @create 2019-08-08 2:44 PM
 **/
public class SocketChannelContext {

    public static Map<String, SocketChannel> clients = new HashMap<String, SocketChannel>();

    public static Map<String, Process> writeMessageQuene = new HashMap<String, Process>();
}

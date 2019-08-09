package com.haibin.im.server.handler;

import com.haibin.im.server.context.SocketChannelContext;
import com.haibin.im.server.protocol.Protocol;
import com.haibin.im.server.util.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 请求处理器
 *
 * @author haibin.tang
 * @create 2019-08-08 11:00 AM
 **/
public class ReaderHandler {

    private static Selector readerSelector;
    private static Selector writerSelector;

    static {
        try {
            readerSelector = Selector.open();
            writerSelector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ReaderHandler() {
        poll();
    }

    public void handle(SocketChannel socketChannel) {
        try {
            socketChannel.register(readerSelector, SelectionKey.OP_READ, socketChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void poll() {
        read();
        write();
    }

    private void read() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        if (readerSelector.selectNow() > 0) {
                            Set<SelectionKey> selectionKeys = readerSelector.selectedKeys();
                            for (SelectionKey selectionKey : selectionKeys) {
                                try {
                                    SocketChannel socketChannel = (SocketChannel) selectionKey.attachment();
                                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
                                    socketChannel.read(byteBuffer);
                                    byteBuffer.flip();
                                    Protocol protocol = JsonUtil.json2Obj(new String(byteBuffer.array()), Protocol.class);
                                    if (protocol == null || StringUtils.isAnyBlank(protocol.getFrom())) {
                                        return;
                                    }
                                    System.out.println("消息内容 -->> " + JsonUtil.obj2Json(protocol));
                                    SocketChannelContext.clients.put(protocol.getFrom(), socketChannel);
                                    if (StringUtils.isAnyBlank(protocol.getTo(), protocol.getBody())) {
                                        return;
                                    }
                                    SocketChannel toSocket = SocketChannelContext.clients.get(protocol.getTo());
                                    if (toSocket != null) {
                                        toSocket.register(writerSelector, SelectionKey.OP_WRITE, new WriteObj(protocol, toSocket));
                                    }
                                } catch (Exception e) {

                                }
                            }
                            selectionKeys.clear();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void write() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        if (writerSelector.selectNow() > 0) {
                            Set<SelectionKey> selectionKeys = writerSelector.selectedKeys();
                            for (SelectionKey selectionKey : selectionKeys) {
                                WriteObj writeObj = (WriteObj) selectionKey.attachment();
                                writeObj.socketChannel.write(ByteBuffer.wrap(JsonUtil.obj2Json(writeObj.getProtocol()).getBytes()));
                                writeObj.socketChannel.keyFor(writerSelector).cancel();
                            }
                            selectionKeys.clear();
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Data
    static class WriteObj {
        private Protocol protocol;
        private SocketChannel socketChannel;

        public WriteObj(Protocol protocol, SocketChannel socketChannel) {
            this.protocol = protocol;
            this.socketChannel = socketChannel;
        }
    }
}

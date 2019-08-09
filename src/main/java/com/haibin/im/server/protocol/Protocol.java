package com.haibin.im.server.protocol;

import lombok.Data;

/**
 * 协议定义
 *
 * @author haibin.tang
 * @create 2019-08-08 10:56 AM
 **/
@Data
public class Protocol {
    private String type;
    private long length;
    private String to;
    private String from;
    private String body;
}

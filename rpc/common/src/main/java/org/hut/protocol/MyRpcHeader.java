package org.hut.protocol;

import lombok.Data;

/**
 * 可增加协议类型，自适应反序列化
 */
@Data
public class MyRpcHeader {
    private String traceId;                 // 用于分布式链路追踪
    private boolean requestFlag;            // 用于标识是请求还是响应
    private String remoteHost;              // 远程 host
    private int port;                       // port
    private String appVersion;              // 用于参数路由
    private String token;                   // 用于请求时的校验
}

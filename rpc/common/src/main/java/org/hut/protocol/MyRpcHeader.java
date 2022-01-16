package org.hut.protocol;

import lombok.Data;

/**
 * 可增加协议类型，自适应反序列化
 */
@Data
public class MyRpcHeader {
    private String traceId;                 // 用于分布式链路追踪
}

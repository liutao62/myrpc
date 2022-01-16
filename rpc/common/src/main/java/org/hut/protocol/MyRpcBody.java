package org.hut.protocol;

import lombok.Data;

/**
 * 远程过程调用请求参数
 */
@Data
public class MyRpcBody {
    private String clzName,                 // 远程过程调用请求类
            methodName;                     // 调用方法
    private Object[] args,                  // 请求参数
            argsType;                       // 请求参数类型
}

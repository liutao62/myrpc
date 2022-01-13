package org.hut.endpoint;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RpcEntity {
    /**
     * rpc 请求参数
     */
    private Object[] args;
    /**
     * rpc 请求参数类型
     */
    private Object[] argTypes;

    public RpcEntity() {

    }

    public RpcEntity(Object[] args) {
        this.args = args;
        argTypes = buildArgTypes(args);
    }

    public RpcEntity(Object[] args, Object[] argTypes) {
        this.args = args;
        this.argTypes = argTypes;
    }

    private Object[] buildArgTypes(Object[] args) {
        if (args == null) {
            return null;
        }
        Object[] argTypes = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            String name = arg.getClass().getName();
            argTypes[i] = name;
        }
        return argTypes;
    }
}

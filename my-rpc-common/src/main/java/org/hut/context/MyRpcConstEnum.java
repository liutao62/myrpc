package org.hut.context;

public enum MyRpcConstEnum {
    RPC_PORT("myrpc.port", "微服务端口"),
    RPC_SCANNER_PACKAGE("myrpc.scanner_package", "微服务扫描包，多个逗号拆分"),
    RPC_REDIS_REGISTER_HOST("myrpc.redis.registry.host", "redis 注册中心 host，如自行实现可不配置"),
    RPC_REDIS_REGISTER_PORT("myrpc.redis.registry.port", "redis port"),
    RPC_REDIS_REGISTER_DATABASE("myrpc.redis.registry.database", "redis database"),
    RPC_REDIS_REGISTER_PASSWORD("myrpc.redis.registry.password", "redis password"),
    RPC_PROJECT_PATH("myrpc.path", "项目当前位置");

    private String define, description;

    MyRpcConstEnum(String define, String description) {
        this.define = define;
        this.description = description;
    }

    public String getDefine() {
        return this.define;
    }
}

package org.hut.register;

public interface RegisterCenter {

    /**
     * 微服务注册
     *
     * @return
     */
    boolean register();

    /**
     * 订阅，接收注册中心服务列表推送处理
     *
     * @return
     */
    boolean sub();

    /**
     * 下线/服务隔离，向注册中心发布下线讯息
     *
     * @return
     */
    boolean pub();
}

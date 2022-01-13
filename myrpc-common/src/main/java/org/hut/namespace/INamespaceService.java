package org.hut.namespace;

import org.hut.namespace.model.MwBean;

import java.util.List;

public interface INamespaceService {

    /**
     * 服务注册
     *
     * @param obj
     * @return
     */
    boolean register(MwBean obj);

    /**
     * 服务脱离，主动下线
     *
     * @param obj
     * @return
     */
    boolean unregister(MwBean obj);

    /**
     * 推送服务列表
     *
     * @return
     */
    List<MwBean> push();

    /**
     * 获取服务提供方代理对象
     *
     * @param namespace
     * @param clz
     * @param <T>
     * @return
     */
    <T> T getRemoteService(String namespace, Class clz);

    /**
     * 心跳检测
     */
    void ping();

}

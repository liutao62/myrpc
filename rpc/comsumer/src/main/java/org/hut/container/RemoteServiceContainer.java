package org.hut.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地维护微服务列表，降低注册中心压力
 */
public class RemoteServiceContainer {
    private static Map<String, List<Object>> CONTAINER = new HashMap<>();

    /**
     * 同步指定服务提供者列表
     *
     * @param namespace
     * @param objects
     */
    public void sync(String namespace, List<Object> objects) {

    }

    /**
     * 同步所有服务提供者列表
     *
     * @param all
     */
    public void sync(Map<String, List<Object>> all) {

    }
}

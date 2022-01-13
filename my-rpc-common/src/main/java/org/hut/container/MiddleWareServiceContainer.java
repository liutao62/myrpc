package org.hut.middleware;

import java.util.HashMap;
import java.util.Map;

public class MiddleWareServiceContainer {
    static Map<String, Object> serviceMap = new HashMap<>();

    public static <T> T getBean(String serviceName) {
        return (T) serviceMap.get(serviceName);
    }

    public static void createBean(String serviceName, Object service) {
        serviceMap.put(serviceName, service);
    }
}

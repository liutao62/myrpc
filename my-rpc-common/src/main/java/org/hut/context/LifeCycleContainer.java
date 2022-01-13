package org.hut.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LifeCycleContainer {
    private static Map<String, MyRpcContextLifeCycle> callBack = new HashMap<>();

    public static void addLifeCycleListener(String serviceName, MyRpcContextLifeCycle listener) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdownEvent()));
        callBack.putIfAbsent(serviceName, listener);
    }

    public static void initEvent() {
        for (MyRpcContextLifeCycle lifeCycle : callBackList) {
            lifeCycle.init();
        }
    }

    public static void startEvent() {
        for (MyRpcContextLifeCycle lifeCycle : callBackList) {
            lifeCycle.start();
        }
    }

    public static void shutdownEvent() {
        for (MyRpcContextLifeCycle lifeCycle : callBackList) {
            lifeCycle.shutdown();
        }
    }

}

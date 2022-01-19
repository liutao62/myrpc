package org.hut.manager;

import lombok.extern.slf4j.Slf4j;
import org.hut.protocol.MyRpcBody;
import org.hut.protocol.MyRpcEntity;
import org.hut.register.RegisterCenter;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ProviderManager extends AbstractManager {

    private static LinkedList<String> inRpcRequestList = new LinkedList<>();
    private static Map<String, String> inRpcRequestMap = new RpcMap<>();

    @Autowired
    private RegisterCenter registerCenter;

    private Lock lock = new ReentrantLock();

    public void init() {
        log.info("ProviderManager init addShutdownHook");
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    public void shutdown() {
        while (!inRpcRequestMap.isEmpty()) {

        }
    }

    @Override
    public void run() {
        shutdown();
    }

    public MyRpcBody dispatchRequest(MyRpcEntity rpcEntity) {
        MyRpcBody body = rpcEntity.getBody();
        String clzName = body.getClzName();
        MyRpcBody responseBody = new MyRpcBody();
        String put = inRpcRequestMap.put(rpcEntity.getHeader().getTraceId(), clzName + "#" + body.getMethodName());
        if (put == null) {
            return responseBody;
        }
        try {
            Class<?> invokeClz = loader.loadClass(clzName);
            Object localService = context.getBean(invokeClz);

            Class[] parameterTypes = null;
            if (body.getArgsType() != null) {
                parameterTypes = new Class[body.getArgsType().length];
                for (int i = 0; i < parameterTypes.length; i++) {
                    parameterTypes[i] = (Class) body.getArgsType()[i];
                }
            }
            Method method = localService.getClass().getMethod(body.getMethodName(), parameterTypes);
            Object invokeResult = method.invoke(localService, body.getArgs());

            responseBody.setArgsType(new Object[]{invokeResult.getClass()});
            responseBody.setArgs(new Object[]{invokeResult});
            return responseBody;
        } catch (Exception e) {
            System.out.println("org.hut.manager.AbstractManager.setApplicationContext ---------" + e);
        } finally {
            inRpcRequestMap.remove(rpcEntity.getHeader().getTraceId());
        }
        return responseBody;
    }


    static class RpcMap<K, V> extends ConcurrentHashMap<K, V> {

        public RpcMap() {

        }

        @Override
        public V put(K key, V value) {
            boolean b = this.containsKey(key);
            if (b) {
                log.info("-----------traceId {} 重复请求,正在处理，本次跳过", key);
                return null;
            }
            log.info("-----------traceId {} 请求 {}", key, value);
            return super.put(key, value);
        }

        @Override
        public V remove(Object key) {
            V v = super.remove(key);
            log.info("-----------traceId {} 请求完毕 {}", key, v);
            return v;
        }
    }
}


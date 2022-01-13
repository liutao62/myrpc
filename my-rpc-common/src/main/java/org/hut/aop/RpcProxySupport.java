package org.hut.aop;

import org.hut.handler.AioHandler;
import org.hut.handler.Handler;
import org.hut.namespace.model.MwBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RpcProxySupport {

    public static <T> T getRemoteService(String namespace, Class clz) {
        return (T) Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz}, new RpcProxy(namespace));
    }

    static class RpcProxy implements InvocationHandler {
        Handler handler = new AioHandler();
        String namespace;

        public RpcProxy(String namespace) {
            this.namespace = namespace;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return handler.handle(new MwBean(namespace, method.getName()), args);
        }
    }
}

package org.hut;

import org.hut.context.MyRpcContext;
import org.hut.entity.Hello;
import org.hut.namespace.INamespaceService;
import org.hut.namespace.impl.NamespaceServiceImpl;
import org.hut.service.IHelloService;

/**
 * Hello world!
 */
public class ComsumerApp {
    public static void main(String[] args) {
        System.setProperty("myrpc.port", "8081");
        MyRpcContext rpcContext = MyRpcContext.getRpcContext();
        rpcContext.start();

        INamespaceService namespaceService = new NamespaceServiceImpl();
        IHelloService helloService = namespaceService.getRemoteService("org.hut.service.IHelloService", IHelloService.class);

        Hello hello = new Hello();
        hello.setName("comsumer");
        Hello hello1 = helloService.sayHello(hello);
        System.out.println(hello1);
    }
}

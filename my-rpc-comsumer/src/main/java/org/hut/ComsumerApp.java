package org.hut;

import org.hut.context.MyRpcContext;
import org.hut.entity.Hello;
import org.hut.registry.RegistryCenter;
import org.hut.service.IHelloService;

/**
 * Hello world!
 */
public class ComsumerApp {
    public static void main(String[] args) {
        MyRpcContext rpcContext = MyRpcContext.loadProperty("myrpc.properties");
        RegistryCenter registryCenter = rpcContext.getRegistryCenter();
        IHelloService helloService = registryCenter.getRemoteService("org.hut.service.IHelloService", IHelloService.class);

        Hello hello = new Hello();
        hello.setName("comsumer");
        Hello hello1 = helloService.sayHello(hello);
        System.out.println(hello1);
    }
}

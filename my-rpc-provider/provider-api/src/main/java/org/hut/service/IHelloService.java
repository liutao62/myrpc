package org.hut.service;

import org.hut.annotation.RemoteCall;
import org.hut.entity.Hello;

@RemoteCall("provider-1111")
public interface IHelloService {
    Hello sayHello(Hello hello);
}

package org.hut.impl;

import lombok.NonNull;
import org.hut.entity.Hello;
import org.hut.service.IHelloService;

public class HelloServiceImpl implements IHelloService {

    @Override
    public Hello sayHello(@NonNull Hello hello) {
        System.out.println(Thread.currentThread().getName() + "---------" + hello.getName());

        Hello hello1 = new Hello();
        hello1.setName("from HelloServiceImpl");
        return hello1;
    }
}

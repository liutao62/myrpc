package org.hut;

import org.hut.endpoint.AioEndPoint;
import org.hut.endpoint.Endpoint;
import org.hut.namespace.model.MwBean;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException {
        Endpoint endpoint = new AioEndPoint();
//        endpoint.start();
        MwBean mwBean = new MwBean("com.org.liutao.TestService", "hello");
        mwBean.setIp("127.0.0.1");
        mwBean.setPort(8080);
        long begin = System.nanoTime();
        Object service = endpoint.service(mwBean, new Object[]{"helle world"});
        System.out.println(System.nanoTime() - begin);
        System.out.println("service result = " + service);
    }
}

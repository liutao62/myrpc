package org.hut.net;

import org.junit.Before;
import org.junit.Test;

public class AbstractEndpointTest {

    AbstractEndpoint endpoint = new BioEndpoint();
    AbstractEndpoint nioEndpoint = new NioEndpoint();

    @Before
    public void setUp() throws Exception {
        System.setProperty("myrpc.port", "8080");
//        endpoint.bind();
        nioEndpoint.bind();
    }

    @Test
    public void accept() {
        // curl 127.0.0.1:8081/uioioi
//        endpoint.accept();
        nioEndpoint.accept();

        System.out.println("test accept after");
    }
}
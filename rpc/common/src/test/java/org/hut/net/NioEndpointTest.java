package org.hut.net;

import org.hut.protocol.MyRpcBody;
import org.hut.protocol.MyRpcEntity;
import org.hut.protocol.MyRpcHeader;
import org.junit.Before;
import org.junit.Test;

public class NioEndpointTest {

    AbstractEndpoint nioEndpoint = new NioEndpoint();

    @Before
    public void setUp() throws Exception {
//        endpoint.bind();
        System.setProperty("myrpc.port","8081");
        nioEndpoint.bind();
    }
    @Test
    public void write() {
        MyRpcEntity rpcEntity = new MyRpcEntity();
        MyRpcBody body = new MyRpcBody();
        body.setClzName("write test");
        MyRpcHeader header = new MyRpcHeader();
        header.setRemoteHost("127.0.0.1");
        header.setPort(8080);
        rpcEntity.setBody(body);
        rpcEntity.setHeader(header);
        nioEndpoint.write(rpcEntity);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {

        }
    }
}
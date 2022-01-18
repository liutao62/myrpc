package org.hut.net;

import org.hut.protocol.MyRpcBody;
import org.hut.protocol.MyRpcEntity;
import org.hut.protocol.MyRpcHeader;
import org.junit.Before;
import org.junit.Test;

public class BioEndpointTest {
    AbstractEndpoint endpoint = new BioEndpoint();

    @Before
    public void setUp() throws Exception {
        System.setProperty("myrpc.port", "8081");
//        endpoint.bind();
    }

    @Test
    public void write() {

        MyRpcEntity rpcEntity = new MyRpcEntity();
        MyRpcBody body = new MyRpcBody();
        body.setClzName("write test");
        MyRpcHeader header = new MyRpcHeader();
        header.setRemoteHost("127.0.0.1");
        header.setPort(8081);
        rpcEntity.setBody(body);
        rpcEntity.setHeader(header);
        MyRpcEntity write = endpoint.write(rpcEntity);
        System.out.println(write);

    }
}
package org.hut.endpoint;

import lombok.Data;

import java.util.Random;

@Data
public class MyRpcRequest {
    private static final Random RANDOM = new Random();
    private Header header = new Header(RANDOM.nextInt(Integer.MAX_VALUE), 0);
    private RpcEntity entity;
}

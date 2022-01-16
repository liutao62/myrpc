package org.hut.protocol;

import java.nio.Buffer;

public class JsonProtocol implements Protocol{
    @Override
    public Buffer serialization(MyRpcRequest request) {
        return null;
    }

    @Override
    public String deserialization(Buffer buffer) {
        return null;
    }
}

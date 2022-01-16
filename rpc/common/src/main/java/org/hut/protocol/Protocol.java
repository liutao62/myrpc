package org.hut.protocol;

import java.nio.Buffer;

public interface Protocol {
    Buffer serialization(MyRpcRequest request);

    String deserialization(Buffer buffer);
}

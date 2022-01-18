package org.hut.protocol;

import java.nio.ByteBuffer;

public interface Protocol {
    ByteBuffer serialization(MyRpcEntity request);

    String deserialization(ByteBuffer buffer);
}

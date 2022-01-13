package org.hut.endpoint;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class RpcEntity {
    private Header header;
    private byte[] bytes;

    public RpcEntity() {

    }

    public RpcEntity(Header header, byte[] bytes) {
        this.header = header;
        this.bytes = bytes;
    }
}

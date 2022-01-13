package org.hut.endpoint;

import lombok.Data;

@Data
public class Header {
    private int traceId;
    private int typeId;

    public Header(int traceId, int typeId) {
        this.traceId = traceId;
        this.typeId = typeId;
    }
}

package org.hut.serialize;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyRequest {
    String namespace, methodName;
    Object[] args;
}

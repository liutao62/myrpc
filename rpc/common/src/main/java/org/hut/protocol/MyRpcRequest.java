package org.hut.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyRpcRequest implements Serializable {
    private static final long serialVersionUID = 5861893135793163392L;

    private MyRpcHeader header;
    private MyRpcBody body;
}

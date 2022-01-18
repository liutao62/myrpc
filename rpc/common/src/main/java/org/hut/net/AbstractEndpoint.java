package org.hut.net;

import org.hut.protocol.JsonProtocol;
import org.hut.protocol.MyRpcEntity;
import org.hut.protocol.Protocol;

import java.net.ServerSocket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public abstract class AbstractEndpoint<S, U> {

    protected ExecutorService EXECUTOR = Executors.newFixedThreadPool(100);

    protected SocketChannel socketChannel;
    protected ServerSocketChannel serverSocketChannel;
    protected ServerSocket socket;
    protected Protocol protocol = new JsonProtocol();

    public abstract void bind();

    protected abstract void accept();

    public abstract void unbind();

    protected abstract MyRpcEntity write(MyRpcEntity rpcEntity);
}

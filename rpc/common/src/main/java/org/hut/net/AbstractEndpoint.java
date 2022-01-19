package org.hut.net;

import org.hut.manager.AbstractManager;
import org.hut.protocol.MyRpcEntity;
import org.hut.protocol.Protocol;
import org.springframework.beans.factory.annotation.Autowired;

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
    @Autowired
    protected Protocol protocol;
    @Autowired
    protected AbstractManager manager;

    public abstract void bind();

    public abstract void accept();

    public abstract void unbind();

    protected abstract MyRpcEntity write(MyRpcEntity rpcEntity);


    public ExecutorService getExecutor() {
        return EXECUTOR;
    }
}

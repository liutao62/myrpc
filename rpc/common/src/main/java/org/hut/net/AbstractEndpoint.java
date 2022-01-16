package org.hut.net;

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

    public abstract void bind();

    protected abstract void accept();

    public abstract void unbind();
}

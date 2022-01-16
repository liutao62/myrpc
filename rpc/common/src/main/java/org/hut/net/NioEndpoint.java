package org.hut.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioEndpoint extends AbstractEndpoint {

    private Selector selector;
    SelectionKey register;

    @Override
    public void bind() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            System.out.println(e);
        }
        initServerSocket();
    }

    private void initServerSocket() {
        try {
            serverSocketChannel = ServerSocketChannel.open();

            InetSocketAddress address = new InetSocketAddress(8081);
            serverSocketChannel.socket().bind(address, 100);
            serverSocketChannel.configureBlocking(false);
            register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {

        }

    }

    @Override
    protected void accept() {
        try {
            while (selector.select() > 0) {
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // SelectionKey.channel返回的channel实例需要强转为我们实际使用的具体的channel类型，
                    // 例如ServerSocketChannel或SocketChannel.

                    if (key.isAcceptable()){
                        System.out.println("-------------isAcceptable");
                    }

                    if (key.isReadable()) {
                        // a channel is ready for reading
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        int read = channel.read(byteBuffer);
                        if (read == -1) {
                            channel.close();
                        } else if (read > 0) {
                            key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                            System.out.println("Get data length: " + read);
                        }
                    } else if (key.isWritable()) {
                        // a channel is ready for writing
                        ByteBuffer buf = (ByteBuffer) key.attachment();
                        buf.flip();
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        clientChannel.write(buf);

                        if (!buf.hasRemaining()) {
                            key.interestOps(SelectionKey.OP_READ);
                        }
                        buf.compact();
                    }
                    keyIterator.remove();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public void unbind() {

    }
}

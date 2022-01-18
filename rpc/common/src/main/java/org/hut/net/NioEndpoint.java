package org.hut.net;

import org.hut.protocol.JsonProtocol;
import org.hut.protocol.MyRpcEntity;
import org.hut.protocol.MyRpcHeader;
import org.hut.protocol.Protocol;

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
    Protocol protocol = new JsonProtocol();

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

            InetSocketAddress address = new InetSocketAddress(Integer.valueOf(System.getProperty("myrpc.port", "8081")));
            serverSocketChannel.bind(address, 100);
            serverSocketChannel.configureBlocking(false);
            register = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            this.socketChannel = SocketChannel.open();
        } catch (IOException e) {
            System.out.println("-------)");
        }

    }

    @Override
    protected void accept() {
        try {
            while (true) {
                if (selector.select(3000) == 1) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    // SelectionKey.channel返回的channel实例需要强转为我们实际使用的具体的channel类型，
                    // 例如ServerSocketChannel或SocketChannel.

                    if (key.isAcceptable()) {
                        System.out.println("-------------isAcceptable");
                        hanldeAccept(key);
                    } else if (key.isReadable()) {
                        // a channel is ready for reading
                        System.out.println("-------------isReadable");
                        handleRead(key);
                    } else if (key.isWritable()) {
                        System.out.println("-------------isWritable");
                        // a channel is ready for writing
                        ByteBuffer byteBuffer = ByteBuffer.wrap("12121212121".getBytes());
                        byteBuffer.flip();
                        SocketChannel clientChannel = (SocketChannel) key.channel();

                        clientChannel.write(byteBuffer);
                        clientChannel.close();
                    }
                    keyIterator.remove();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
        // 是否
        boolean requestFlag = byteBuffer != null;
        byteBuffer = ByteBuffer.allocate(1024);
        int read = channel.read(byteBuffer);
        StringBuilder builder = new StringBuilder();
        while (read > 0) {
            byteBuffer.flip();
            String deserialization = protocol.deserialization(byteBuffer);
            System.out.println("-------deserialization" + deserialization);
            builder.append(deserialization);
            byteBuffer.clear();
            read = channel.read(byteBuffer);
        }
        if (requestFlag) {
            channel.register(selector, SelectionKey.OP_WRITE, true);
        } else {
            channel.write(ByteBuffer.wrap("hello -------------".getBytes()));
            channel.close();
        }
    }

    private void hanldeAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();

        SocketChannel accept = channel.accept();
        accept.configureBlocking(false);

        accept.register(selector, SelectionKey.OP_READ, key.attachment());
    }

    @Override
    public void unbind() {

    }

    @Override
    protected void write(MyRpcEntity rpcEntity) {
        MyRpcHeader header = rpcEntity.getHeader();
        String remoteHost = header.getRemoteHost();
        int port = header.getPort();
        try {
            InetSocketAddress address = new InetSocketAddress(remoteHost, port);
            socketChannel.configureBlocking(false);
            socketChannel.connect(address);
            socketChannel.register(selector, SelectionKey.OP_READ, rpcEntity);
        } catch (IOException e) {
            System.out.println("write-----" + e);
        }


    }
}

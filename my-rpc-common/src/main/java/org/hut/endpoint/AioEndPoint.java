package org.hut.endpoint;

import com.alibaba.fastjson.JSONObject;
import org.hut.context.MyRpcConstEnum;
import org.hut.context.MyRpcContext;
import org.hut.handler.AioHandler;
import org.hut.handler.Handler;
import org.hut.registry.RegistryCenter;
import org.hut.registry.model.MwBean;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AioEndPoint extends AbstractEndpoint {

    MyRpcContext.getRpcContext();
    
    static AsynchronousChannelGroup channelGroup;

    static {
        try {
            channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
        } catch (IOException e) {

        }
    }

    //    private int port = 8081;
    AsynchronousServerSocketChannel serverSocketChannel = null;
    AsynchronousSocketChannel socketChannel = null;
    private Handler handler = new AioHandler();
    private int port = Integer.valueOf(System.getProperty(MyRpcConstEnum.RPC_PORT.getDefine()));

    public AioEndPoint() {
        init();
    }


    @Override
    public void init() {
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));

            AcceptCompletionHandler acceptCompletionHandler = new AcceptCompletionHandler(serverSocketChannel);
            serverSocketChannel.accept(null, acceptCompletionHandler);

            socketChannel = AsynchronousSocketChannel.open(channelGroup);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        try {
            serverSocketChannel.close();
            socketChannel.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public <T> T service(MwBean mwBean, Object[] args) {
        // todo 序列化动态支持，返回序列化类型

        MyRpcRequest request = new MyRpcRequest();
        request.setEntity(new RpcEntity(args));

//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//        byteBuffer.put(rpcEntity.toString().getBytes());

        Charset charset = Charset.forName("ISO-8859-1");
        ByteBuffer encode = charset.encode(JSONObject.toJSONString(request));

        StringBuilder responseBuilder = new StringBuilder();
        try {
//            ConnectCompletionHandler handler = new ConnectCompletionHandler(socketChannel, encode);
            InetSocketAddress socketAddress = new InetSocketAddress(mwBean.getIp(), mwBean.getPort());
//            socketChannel.connect(socketAddress, null, handler);
            // 这里 BIO 了
            socketChannel.connect(socketAddress).get(1, TimeUnit.SECONDS);//block until the connection is established
            tryRpc(charset, encode, responseBuilder);

            System.out.println(responseBuilder);
        } catch (Exception e) {
            System.out.println("service ---" + e);
            try {
                tryRpc(charset, encode, responseBuilder);
            } catch (Exception exception) {
                System.out.println("service exception---" + e);
            }
        }
        JSONObject response = (JSONObject) JSONObject.parse(responseBuilder.toString());
        Object resultType = response.get("resultType");
        Object result = response.get("result");
        Class<?> aClass = null;
        try {
            aClass = Class.forName(resultType.toString());
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }

        return (T) JSONObject.parseObject(result.toString(), aClass);
    }

    private void tryRpc(Charset charset, ByteBuffer encode, StringBuilder responseBuilder) throws InterruptedException, ExecutionException {
        Integer bytesWritten = socketChannel.write(encode).get();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//            socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
//                        public void completed(Integer bytesRead, ByteBuffer buffer) {
//                            buffer.flip();
//                            responseBuilder.append(StandardCharsets.ISO_8859_1.decode(buffer).toString());
//                            try {
//                                socketChannel.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        public void failed(Throwable exc, ByteBuffer attachment) {
//                            try {
//                                socketChannel.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//            );
        if (socketChannel.read(byteBuffer).get() != -1) {
            byteBuffer.flip();
            CharBuffer decode = charset.decode(byteBuffer);
            responseBuilder.append(decode);
        }
    }

    /**
     * 服务端
     */
    class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

        private final AsynchronousServerSocketChannel serverSocketChannel;

        AcceptCompletionHandler(AsynchronousServerSocketChannel serverSocketChannel) {
            this.serverSocketChannel = serverSocketChannel;
        }

        @Override
        public void completed(AsynchronousSocketChannel socketChannel, Void attachment) {
            serverSocketChannel.accept(null, this); // non-blocking
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            ReadCompletionHandler readCompletionHandler = new ReadCompletionHandler(socketChannel, buffer);
            socketChannel.read(buffer, null, readCompletionHandler); // non-blocking
        }

        @Override
        public void failed(Throwable t, Void attachment) {
            // exception handling
            System.out.println("org.hut.liutao.nio2.AcceptCompletionHandler.failed");
        }
    }

    class ReadCompletionHandler implements CompletionHandler<Integer, Void> {

        private final AsynchronousSocketChannel socketChannel;
        private ByteBuffer buffer;

        ReadCompletionHandler(AsynchronousSocketChannel socketChannel, ByteBuffer buffer) {
            this.socketChannel = socketChannel;
            this.buffer = buffer;
        }

        @Override
        public void completed(Integer bytesRead, Void attachment) {
            WriteCompletionHandler writeCompletionHandler = new WriteCompletionHandler(socketChannel);
            buffer.flip();
            Charset charset = Charset.forName("ISO-8859-1");
            String text = charset.decode(buffer).toString();

            System.out.println("text = " + text);

            if (text != null && text.startsWith("{")) {
                JSONObject request = (JSONObject) JSONObject.parse(text);
                MwBean mwBean = JSONObject.parseObject(request.get("mwBean").toString(), MwBean.class);
                Object bean = RegistryCenter.getBean(mwBean.getNamespace());
                Method method = null;
                Object executorResult = null;
                try {
                    method = Arrays.stream(bean.getClass().getMethods()).filter(method1 -> method1.getName().equals(mwBean.getMethodName())).findAny().get();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    List<Object> args = new ArrayList<>();
                    if (parameterTypes != null) {
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> type = parameterTypes[i];
                            Object o = request.get("arg" + i);
                            Object o1 = JSONObject.parseObject(o.toString(), type);
                            args.add(o1);
                        }
                    }
                    executorResult = method.invoke(bean, args.toArray());
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (executorResult != null) {
                    System.out.println(executorResult);
                    JSONObject result = new JSONObject();
                    result.put("result", executorResult);
                    result.put("resultType", executorResult.getClass().getName());

                    ByteBuffer world = charset.encode(JSONObject.toJSONString(result));
                    socketChannel.write(world, null, writeCompletionHandler); // non-blocking
                }
            } else {

            }
        }

        @Override
        public void failed(Throwable t, Void attachment) {
            System.out.println("org.hut.liutao.nio2.ReadCompletionHandler.failed");
            // exception handling
        }
    }

    class WriteCompletionHandler implements CompletionHandler<Integer, Void> {

        private final AsynchronousSocketChannel socketChannel;

        WriteCompletionHandler(AsynchronousSocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void completed(Integer bytesWritten, Void attachment) {
            System.out.println("org.hut.liutao.nio2.WriteCompletionHandler.completed");
            try {
                ByteBuffer allocate = ByteBuffer.allocate(1024);
                socketChannel.read(allocate, null, new ReadCompletionHandler(socketChannel, allocate)); // non-blocking
            } catch (Exception e) {
                // exception handling
            }
        }

        @Override
        public void failed(Throwable t, Void attachment) {
            // exception handling
            System.out.println("org.hut.liutao.nio2.WriteCompletionHandler.failed");
        }
    }

    class ConnectCompletionHandler implements CompletionHandler<Void, Void> {

        AsynchronousSocketChannel socketChannel;
        ByteBuffer byteBuffer;

        public ConnectCompletionHandler(AsynchronousSocketChannel socketChannel, ByteBuffer byteBuffer) {
            this.socketChannel = socketChannel;
            this.byteBuffer = byteBuffer;
        }

        @Override
        public void completed(Void result, Void attachment) {
            System.out.println("org.hut.endpoint.AioEndPoint.ConnectCompletionHandler.completed");
            socketChannel.write(byteBuffer, null, new WriteCompletionHandler(socketChannel));
        }

        @Override
        public void failed(Throwable exc, Void attachment) {

        }
    }
}

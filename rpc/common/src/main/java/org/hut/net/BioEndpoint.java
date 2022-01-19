package org.hut.net;

import org.hut.protocol.MyRpcEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class BioEndpoint extends AbstractEndpoint {

    @Override
    public void bind() {
        initSocket();
    }

    @Override
    public void accept() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Socket clientSocket = socket.accept();
                EXECUTOR.submit(new ConnectIOnHandler(clientSocket));
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    private void initSocket() {
        try {
            String port = System.getProperty("myrpc.port", "8081");
            socket = new ServerSocket(Integer.valueOf(port));
        } catch (IOException e) {

        }
    }


    @Override
    public void unbind() {

    }

    @Override
    protected MyRpcEntity write(MyRpcEntity rpcEntity) {
        Socket socket = new Socket();
        try {

//            socket.bind(new InetSocketAddress(rpcEntity.getHeader().getRemoteHost(), rpcEntity.getHeader().getPort()));
            socket.connect(new InetSocketAddress(rpcEntity.getHeader().getRemoteHost(), rpcEntity.getHeader().getPort()), 2000);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(protocol.serialization(rpcEntity).array());
            InputStream inputStream = socket.getInputStream();
            while (inputStream.available() == 0){

            }
            byte[] bytes = new byte[inputStream.available()];
            System.out.println("available == " + inputStream.available());
            int read = inputStream.read(bytes);
            String deserialization = protocol.deserialization(ByteBuffer.wrap(bytes));
            MyRpcEntity data = protocol.getData(deserialization);
            return data;
        } catch (Exception e) {
            System.out.println("org.hut.net.BioEndpoint.write    " + e);
        }
        return null;
    }

    class ConnectIOnHandler extends Thread {
        private Socket socket;

        public ConnectIOnHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
                InputStream inputStream = null;
                try {
                    inputStream = socket.getInputStream();

                    byte[] bytes = new byte[inputStream.available()];
                    System.out.println("-------- reading --------");
                    inputStream.read(bytes);
                    String s = new String(bytes, "UTF-8");
                    System.out.println(s);

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write("hello BIO".getBytes());
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

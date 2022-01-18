package org.hut.net;

import org.hut.protocol.MyRpcEntity;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BioEndpoint extends AbstractEndpoint {

    @Override
    public void bind() {
        initSocket();
    }

    @Override
    protected void accept() {
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
    protected void write(MyRpcEntity rpcEntity) {

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
                    outputStream.write("hello BIO".getBytes(StandardCharsets.UTF_8));
                    socket.close();
                } catch (IOException e) {

                }
            }
        }
    }
}

package org.hut.managerment;

import org.hut.register.RegisterCenter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProviderManager implements Runnable {

    private static LinkedList<String> inRpcRequestList = new LinkedList<>();

    @Autowired
    private RegisterCenter registerCenter;

    private Lock lock = new ReentrantLock();

    public void init() {
        Runtime.getRuntime().addShutdownHook(new Thread(this));
    }

    public void shutdown() {

    }

    @Override
    public void run() {
        shutdown();
    }

    class Handler {

    }
}


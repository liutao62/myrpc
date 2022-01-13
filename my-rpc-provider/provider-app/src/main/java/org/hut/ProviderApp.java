package org.hut;

import org.hut.context.MyRpcContext;

/**
 * Hello world!
 */
public class ProviderApp {

    public static void main(String[] args) {
        MyRpcContext.loadProperty("myrpc.properties");
    }
}

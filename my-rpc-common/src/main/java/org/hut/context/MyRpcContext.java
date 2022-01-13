package org.hut.context;

import org.hut.endpoint.AioEndPoint;
import org.hut.endpoint.Endpoint;
import org.hut.namespace.INamespaceService;
import org.hut.namespace.impl.NamespaceServiceImpl;

public class MyRpcContext {

    private static final MyRpcContext RPC_CONTEXT = new MyRpcContext();

    Endpoint endpoint;

    INamespaceService namespaceService;

    private MyRpcContext() {
    }

    public static MyRpcContext getRpcContext() {
        return RPC_CONTEXT;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void start() {
        endpoint = new AioEndPoint();
        namespaceService = new NamespaceServiceImpl();
    }

    public INamespaceService getNamespaceService() {
        return this.namespaceService;
    }

}

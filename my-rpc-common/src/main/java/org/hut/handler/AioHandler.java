package org.hut.handler;

import org.hut.context.MyRpcContext;
import org.hut.endpoint.Endpoint;
import org.hut.registry.model.MwBean;

import java.util.HashMap;
import java.util.Map;

public class AioHandler extends AbstractHandler {

    Endpoint endpoint = MyRpcContext.getRpcContext().getEndpoint();

    static Map<String, Object> requestMapping = new HashMap<>();

    @Override
    public <T> T handle(MwBean mwBean, Object[] args) {
        String methodName = mwBean.getMethodName();
        mwBean = super.balancePlugin(mwBean.getNamespace());
        mwBean.setMethodName(methodName);
        return endpoint.service(mwBean, args);
    }

    @Override
    public <T> T dispatch(MwBean mwBean, Object[] args) {
        return null;
    }
}


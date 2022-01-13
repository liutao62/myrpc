package org.hut.endpoint;

import org.hut.context.MyRpcContextLifeCycle;
import org.hut.registry.RegistryCenter;
import org.hut.registry.model.MwBean;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractEndpoint implements Endpoint {

    private List<MyRpcContextLifeCycle> lifeCycleList = new ArrayList<>();

    private Map<MwBean, Socket> mwBeanChannelMap = new ConcurrentHashMap<>();

    protected Socket switchScoket(MwBean mwBean) {
        Socket socket = mwBeanChannelMap.get(mwBean);
        if (socket == null) {
            try {
                socket = new Socket(mwBean.getIp(), mwBean.getPort());
                socket.setKeepAlive(true);
                socket.setSoTimeout(60000);
                return socket;
            } catch (Exception e) {
                throw new RuntimeException("注册中心找不到可用实例", e);
            }
        }
        return socket;
    }

}

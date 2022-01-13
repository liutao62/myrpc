package org.hut.handler;

import org.apache.commons.collections4.CollectionUtils;

import org.hut.context.MyRpcContext;
import org.hut.namespace.INamespaceService;
import org.hut.namespace.model.MwBean;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractHandler implements Handler {

    protected Map<String, Set<MwBean>> mwServiceMap = new ConcurrentHashMap<>();
    private INamespaceService namespaceService = MyRpcContext.getRpcContext().getNamespaceService();

    public MwBean balancePlugin(String namespace) {
        Set<MwBean> mwBeans = mwServiceMap.get(namespace);
        if (CollectionUtils.isEmpty(mwBeans)){
            // 注册中心拉取 todo zset
            List<MwBean> push = namespaceService.push();
            if (CollectionUtils.isEmpty(push)){
                throw new RuntimeException("注册中心未发现可用实例 " + namespace);
            }
            mwBeans = new HashSet<>(push);
        }
        // 负载均衡插件调用

        return mwBeans.stream().filter(mwBean -> mwBean.getIp() != null).findAny().get();
    }
}

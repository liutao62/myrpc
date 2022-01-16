package org.hut.balance;

import org.hut.exception.MyRpcException;

import java.util.List;

public class DefaultBalancePlugin implements LoadBalancePlugin {
    @Override
    public Object balance(List<Object> objects) {
        if (objects == null || objects.size() == 0) {
            throw new MyRpcException("注册中心未找到可用实例");
        }
        return null;
    }
}

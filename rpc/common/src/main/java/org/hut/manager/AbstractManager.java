package org.hut.manager;

import org.hut.protocol.MyRpcBody;
import org.hut.protocol.MyRpcEntity;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractManager implements ApplicationContextAware, Runnable {

    protected ApplicationContext context;
    protected ClassLoader loader = this.getClass().getClassLoader();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public abstract MyRpcBody dispatchRequest(MyRpcEntity rpcEntity);

    public abstract void init();

}

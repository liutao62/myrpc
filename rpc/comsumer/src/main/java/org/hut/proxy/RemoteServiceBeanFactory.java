package org.hut.proxy;

import org.springframework.beans.factory.FactoryBean;

public class RemoteServiceBeanFactory implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

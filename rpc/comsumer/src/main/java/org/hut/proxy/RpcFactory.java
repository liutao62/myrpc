package org.hut.proxy;

import org.hut.exception.MyRpcException;
import org.hut.model.RemoteCallInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.List;

public class RpcFactory implements BeanDefinitionRegistryPostProcessor {

    private String namespace;
    private List<RemoteCallInfo> remoteCallInfoList;

    public RpcFactory(String namespace, List<RemoteCallInfo> remoteCallInfoList) {
        this.namespace = namespace;
        this.remoteCallInfoList = remoteCallInfoList;
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        if (remoteCallInfoList != null && remoteCallInfoList.size() > 0) {
            try {
                for (RemoteCallInfo remoteCallInfo : remoteCallInfoList) {
                    Class<?> serviceInterface = this.getClass().getClassLoader()
                            .loadClass(remoteCallInfo.getRemoteServiceName());

                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(serviceInterface);
                    GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();

                    definition.getConstructorArgumentValues().addGenericArgumentValue(serviceInterface);

                    definition.setBeanClass(RemoteServiceBeanFactory.class);
                    definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
                    beanDefinitionRegistry.registerBeanDefinition(serviceInterface.getSimpleName(), definition);
                }
            } catch (Exception e) {
                throw new MyRpcException(e);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}

package org.hut.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class RpcFactory implements BeanDefinitionRegistryPostProcessor {

    List<RemoteCallInfo> remoteCallInfoList;
    String namespace;

    public RpcFactory(List<RemoteCallInfo> remoteCallInfoList, String namespace) {
        this.remoteCallInfoList = remoteCallInfoList;
        this.namespace = namespace;
    }


    @Override

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        System.out.println("----------postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}

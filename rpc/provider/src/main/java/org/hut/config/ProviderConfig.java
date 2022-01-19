package org.hut.config;

import org.hut.manager.AbstractManager;
import org.hut.manager.ProviderManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:myrpc/*")
public class ProviderConfig {

    @Bean(initMethod = "init")
    public AbstractManager getProviderManager(){
        return new ProviderManager();
    }
}

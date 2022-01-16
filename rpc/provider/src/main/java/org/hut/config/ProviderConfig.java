package org.hut.config;

import org.hut.managerment.ProviderManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProviderConfig {

    @Bean(initMethod = "init")
    public ProviderManager getProviderManager(){
        return new ProviderManager();
    }
}

package org.hut.config;

import org.hut.protocol.JsonProtocol;
import org.hut.protocol.Protocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRpcConfig {

    @Bean
    @ConditionalOnMissingBean(name = "protocol")
    public Protocol getProtocol() {
        return new JsonProtocol();
    }
}

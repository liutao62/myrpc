package org.hut.config;

import org.hut.net.AbstractEndpoint;
import org.hut.net.NioEndpoint;
import org.hut.protocol.JsonProtocol;
import org.hut.protocol.Protocol;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;

@Configuration
public class MyRpcConfig {

    @Bean
    @ConditionalOnMissingBean(name = "protocol")
    public Protocol getProtocol() {
        return new JsonProtocol();
    }

    @Bean
    public AbstractEndpoint getEndPoint() {
        AbstractEndpoint endpoint = new NioEndpoint();
        ExecutorService executor = endpoint.getExecutor();
        executor.execute(new Thread(() -> {
            endpoint.bind();
            endpoint.accept();
        }));
        return endpoint;
    }
}

package org.hut.endpoint;

import org.hut.namespace.model.MwBean;

public interface Endpoint {

    void start();

    void stop();

    <T> T service(MwBean mwBean, Object[] args);
}


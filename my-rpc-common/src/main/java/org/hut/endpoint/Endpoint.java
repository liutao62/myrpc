package org.hut.endpoint;

import org.hut.context.MyRpcContextLifeCycle;
import org.hut.registry.model.MwBean;

public interface Endpoint extends MyRpcContextLifeCycle {

    <T> T service(MwBean mwBean, Object[] args);
}


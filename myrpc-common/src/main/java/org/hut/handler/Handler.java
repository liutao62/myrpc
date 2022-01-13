package org.hut.handler;

import org.hut.namespace.model.MwBean;

public interface Handler {

    <T> T handle(MwBean mwBean, Object[] args);

    <T> T dispatch(MwBean mwBean, Object[] args);
}

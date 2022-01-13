package org.hut.namespace.model;

import lombok.Data;

import java.util.Objects;

@Data
public class MwBean {
    private String ip,
            namespace,
            methodName;

    private int port,
            weight;

    public MwBean() {

    }

    public MwBean(String namespace, String methodName) {
        this.namespace = namespace;
        this.methodName = methodName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MwBean mwBean = (MwBean) o;
        return port == mwBean.port && Objects.equals(ip, mwBean.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, port);
    }
}

package org.hut.registry.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class MwBean {
    private String ip,
            namespace,
            methodName;

    private int port,
            weight = 1;

    public MwBean() {

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

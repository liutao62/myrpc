package org.hut.balance;

import java.util.List;

@FunctionalInterface
public interface LoadBalancePlugin {
    Object balance(List<Object> objects);
}

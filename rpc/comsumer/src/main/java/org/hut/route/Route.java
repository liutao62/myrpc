package org.hut.route;

import java.util.List;
import java.util.Objects;

/**
 * 路由策略
 */
@FunctionalInterface
public interface Route {

    /**
     * 根据路由策略从服务列表中筛选路由结果
     *
     * @param objects
     * @return
     */
    List<Objects> route(List<Objects> objects);
}

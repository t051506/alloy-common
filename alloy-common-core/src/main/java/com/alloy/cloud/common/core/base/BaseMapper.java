package com.alloy.cloud.common.core.base;

import java.io.Serializable;
import java.util.List;

public interface BaseMapper<ID extends Serializable, T extends BaseEntity> {
    boolean insert(T u);

    boolean deleteById(ID id);

    boolean update(T u);

    T queryById(ID id);

    List<T> queryAll();
}

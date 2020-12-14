package com.alloy.cloud.common.core.base;

import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: tankechao
 * @Date: 2020/12/14 13:16
 */
public class BaseServiceImpl<ID extends Serializable, T extends BaseEntity> implements BaseService<ID, T> {
    @Resource
    private BaseMapper<ID, T> baseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(T u) {
        return baseMapper.insert(u);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(ID id) {
        return baseMapper.deleteById(id);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public boolean update(T u) {
        return baseMapper.update(u);
    }

    @Override
    public T queryById(ID id) {
        return baseMapper.queryById(id);
    }

    @Override
    public List<T> queryAll() {
        return baseMapper.queryAll();
    }
}

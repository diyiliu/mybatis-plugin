package com.diyiliu.bll.service;


import com.diyiliu.bll.bean.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * Description: BaseService
 * Author: DIYILIU
 * Update: 2016-04-07 9:56
 */

public interface BaseService<T extends BaseEntity> {

    void insert(T entity);

    void delete(T entity);

    void update(T entity);

    T select(T entity);

    List<T> selectForList(T entity);

    void batchDelete(String table, String key, String[] keys);

    List<Map> selectBySql(String sql);
}

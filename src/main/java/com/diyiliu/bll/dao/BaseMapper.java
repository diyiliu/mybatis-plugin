package com.diyiliu.bll.dao;

import com.diyiliu.bll.bean.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description: BaseMapper
 * Author: DIYILIU
 * Update: 2016-04-05 15:51
 */
public interface BaseMapper<T extends BaseEntity> {

    void insertEntity(T entity);

    void deleteEntity(T entity);

    void updateEntity(T entity);

    List<T> selectEntity(T entity);

    void batchDelete(@Param("table") String table, @Param("key") String key, @Param("keys") String[] keys);

    List<Map> selectBySql(@Param("sql") String sql);
}

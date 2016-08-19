package com.diyiliu.bll.service.impl;

import com.diyiliu.bll.bean.BaseEntity;
import com.diyiliu.bll.dao.BaseMapper;
import com.diyiliu.bll.service.BaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description: BaseServiceImpl
 * Author: DIYILIU
 * Update: 2016-04-07 9:50
 */

@Service
public class  BaseServiceImpl implements BaseService {

    @Resource
    private BaseMapper baseDao;

    @Override
    public void insert(BaseEntity entity) {

        baseDao.insertEntity(entity);
    }

    @Override
    public void delete(BaseEntity entity) {

        baseDao.deleteEntity(entity);
    }

    @Override
    public void update(BaseEntity entity) {

        baseDao.updateEntity(entity);
    }

    @Override
    public List select(BaseEntity entity) {

        return baseDao.selectEntity(entity);
    }

    @Override
    public void batchDelete(String table, String key, String[] keys) {

        baseDao.batchDelete(table, key, keys);
    }

    @Override
    public List<Map> selectBySql(String sql) {
        return baseDao.selectBySql(sql);
    }
}

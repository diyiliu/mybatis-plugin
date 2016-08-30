package com.diyiliu.bll.bean;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: BaseEntity
 * Author: DIYILIU
 * Update: 2016-08-19 15:29
 */

public class BaseEntity implements Serializable {

    protected String orderBy;

    public HashMap toHashMap() {

        HashMap map = new HashMap();
        try {
            Field[] fields = this.getClass().getDeclaredFields();

            String column;
            Object value;
            for (Field field : fields) {

                if (field.isAccessible()) {
                    Column columnField = field.getAnnotation(Column.class);
                    if (columnField != null) {
                        column = columnField.name();
                        value = field.get(this);

                        map.put(column, value);
                    }
                } else {
                    field.setAccessible(true);
                    Column columnField = field.getAnnotation(Column.class);
                    if (columnField != null) {
                        column = columnField.name();
                        value = field.get(this);

                        map.put(column, value);
                    }

                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    public List toEntity(ResultSet resultSet) {
        Field[] fields = this.getClass().getDeclaredFields();

        List list = new ArrayList();
        String column;
        Object value;
        try {
            while (resultSet.next()) {
                Object obj = this.getClass().newInstance();
                for (Field field : fields) {
                    if (field.isAccessible()) {
                        Column columnField = field.getAnnotation(Column.class);
                        if (columnField != null) {
                            column = columnField.name();
                            value = resultSet.getObject(column);

                            field.set(obj, value);
                        }
                    } else {
                        field.setAccessible(true);
                        Column columnField = field.getAnnotation(Column.class);
                        if (columnField != null) {
                            column = columnField.name();
                            value = resultSet.getObject(column);

                            field.set(obj, value);
                        }

                        field.setAccessible(false);
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}

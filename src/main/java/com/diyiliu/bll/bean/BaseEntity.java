package com.diyiliu.bll.bean;

import com.diyiliu.other.Criteria;

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
                    Column columnField = field.getAnnotation(Column.class);

                    if (field.isAccessible()) {
                        if (columnField != null) {
                            column = columnField.name();
                            value = resultSet.getObject(column);

                            field.set(obj, value);
                        }
                    } else {
                        field.setAccessible(true);
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


    private List<Criteria> criList = new ArrayList<Criteria>();

    public List<Criteria> getCriList() {
        return criList;
    }

    public void setWhere(String symbol, String field, Object... value) {

        try {
            Field f = this.getClass().getDeclaredField(field);
            Column c = f.getAnnotation(Column.class);

            criList.add(new Criteria(symbol, c.name(), value));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public void setWhere(boolean region, String symbol, String field, Object... value) {

        if (region) {
            criList.add(new Criteria(symbol, field, value));
        } else {
            setWhere(symbol, field, value);
        }
    }


}

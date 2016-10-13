package com.diyiliu.bll.bean;

import com.diyiliu.other.Criteria;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Description: BaseEntity
 * Author: DIYILIU
 * Update: 2016-08-19 15:29
 */

public class BaseEntity<T extends BaseEntity> implements Serializable {

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
                    if (columnField == null) {
                        continue;
                    }
                    column = columnField.name();
                    value = formatResultSet(field, column, resultSet);

                    if (field.isAccessible()) {

                        field.set(obj, value);
                    } else {

                        field.setAccessible(true);

                        field.set(obj, value);

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

    private Object formatResultSet(Field field, String column, ResultSet resultSet) {

        String fieldType = field.getType().getTypeName();

        Object result = null;
        try {
            if (fieldType.contains("int") || fieldType.contains("Integer")) {
                result = resultSet.getInt(column);
            } else {
                result = resultSet.getObject(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<Criteria> criList = new ArrayList<Criteria>();

    public List<Criteria> getCriList() {
        return criList;
    }

    public T setWhere(String symbol, String field, Object... value) {

        criList.add(new Criteria(symbol, field, value));

        return (T) this;
    }

    public T setWhere(boolean region, String symbol, String field, Object... value) {

        if (region) {
            field = fetchColumn(field);
        }

        setWhere(symbol, field, value);

        return (T) this;
    }

    public T setWhere(boolean or, boolean region, String symbol, String field, Object... value) {

        if (region) {
            field = fetchColumn(field);
        }

        Criteria c = new Criteria(symbol, field, value);
        if (or) {
            c.setOr(true);
        }
        criList.add(c);

        return (T) this;
    }

    private String fetchColumn(String field) {

        try {
            Field f = this.getClass().getDeclaredField(field);
            Column c = f.getAnnotation(Column.class);

            return c.name();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}

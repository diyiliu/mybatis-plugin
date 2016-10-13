package com.diyiliu.plugin;

import com.diyiliu.bll.bean.BaseEntity;
import com.diyiliu.other.Constant;
import com.diyiliu.other.Criteria;
import com.diyiliu.other.DateUtil;
import com.diyiliu.plugin.abs.SPlugin;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import javax.persistence.Table;
import java.sql.Connection;
import java.util.*;

/**
 * Description: BasePlugin
 * Author: DIYILIU
 * Update: 2016-02-19 11:22
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class BasePlugin extends SPlugin {

    private String baseSqlId;
    protected String dialect;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        strip(invocation.getTarget());

        MappedStatement mappedStatement = (MappedStatement) getValue("mappedStatement");

        String sqlId = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);

        if (sqlId.matches(baseSqlId)) {
            logger.info("Mybatis 通用插件...");

            BoundSql boundSql = (BoundSql) getValue("boundSql");
            Object parameterObject = boundSql.getParameterObject();

            if (parameterObject instanceof BaseEntity) {
                setValue("boundSql.sql", joinSql(sqlId, (BaseEntity) parameterObject));
            }
        }

        return invocation.proceed();
    }

    public String joinSql(String sqlId, BaseEntity entity) {

        Table tableField = entity.getClass().getAnnotation(Table.class);
        String table = tableField.name();
        String key = tableField.schema();

        HashMap map = entity.toHashMap();
        String[] fields = new String[map.size()];
        Object[] values = new Object[map.size()];

        int i = 0;
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            String column = (String) iter.next();
            fields[i] = column;
            values[i] = map.get(column);
            i++;
        }

        StringBuilder sbd;
        if (sqlId.equals(Constant.Crud.INSERT)) {

            sbd = new StringBuilder("INSERT INTO ");
            sbd.append(table).append("(");
            for (String field : fields) {
                sbd.append(field).append(",");
            }
            sbd = sbd.replace(sbd.length() - 1, sbd.length(), ")");
            sbd.append("VALUES(");
            for (Object value : values) {
                sbd.append(format(value)).append(",");
            }
            sbd = sbd.replace(sbd.length() - 1, sbd.length(), ")");

            return sbd.toString();
        } else if (sqlId.equals(Constant.Crud.DELETE)) {

            sbd = new StringBuilder("DELETE FROM ");
            sbd.append(table).append(" WHERE ").append(key).append("=").append(format(map.get(key)));

            return sbd.toString();
        } else if (sqlId.equals(Constant.Crud.UPDATE)) {

            sbd = new StringBuilder("UPDATE ");
            sbd.append(table).append(" SET ");
            for (String field : fields) {
                sbd.append(field).append("=").append(format(map.get(field))).append(", ");
            }
            int index = sbd.lastIndexOf(",");
            sbd.replace(index, index + 1, "");
            sbd.append(" WHERE 1=1 ");

            if (entity.getCriList().size() > 0) {
                sbd.append(joinCriteria(entity.getCriList()));
            }

            return sbd.toString();
        } else if (sqlId.equals(Constant.Crud.SELECT) || sqlId.equals(Constant.Crud.SELECT_LIST)) {
            sbd = new StringBuilder("SELECT * FROM ");
            sbd.append(table);
            sbd.append(" WHERE 1=1 ");

            if (entity.getCriList().size() > 0) {
                sbd.append(joinCriteria(entity.getCriList()));
            }

            return sbd.toString();
        }

        return null;
    }

    private String joinCriteria(List<Criteria> list) {

        Criteria order = null;

        StringBuilder sbd = new StringBuilder();
        for (Criteria c : list) {

            String link = c.getOr() ? " OR " : " AND ";
            switch (c.getSymbol()) {
                case Constant.QBuilder.EQUAL:
                    sbd.append(link).append(c.getKey()).append("=").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.NOT_EQUAL:
                    sbd.append(link).append(c.getKey()).append("<>").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.GREATER:
                    sbd.append(link).append(c.getKey()).append(">").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.GREATER_OR_EQUAL:
                    sbd.append(link).append(c.getKey()).append(">=").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.LESS:
                    sbd.append(link).append(c.getKey()).append("<").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.LESS_OR_EQUAL:
                    sbd.append(link).append(c.getKey()).append("<=").append(format(c.getValue()[0]));
                    break;
                case Constant.QBuilder.IN:
                    sbd.append(link).append(c.getKey()).append(" IN ").append(format(c.getValue(), "(", ")", ","));
                    break;
                case Constant.QBuilder.NOT_IN:
                    sbd.append(link).append(c.getKey()).append(" NOT IN ").append(format(c.getValue(), "(", ")", ","));
                    break;
                case Constant.QBuilder.RECURSIVE:
                    sbd.append(link).append("( 1=1 ").append(joinCriteria(Arrays.asList((Criteria[]) c.getValue()))).append(")");
                    break;
                case Constant.QBuilder.ORDER_BY:
                    order = c;
                    break;
                default:
                    break;
            }
        }

        if (order != null) {
            StringBuilder builder = new StringBuilder();
            for (Object o: order.getValue()){
                builder.append(o).append(",");
            }
            sbd.append(" ORDER BY ").append(builder.substring(0, builder.length() - 1));
        }

        return sbd.toString();
    }

    private String format(Object[] value, String prefix, String suffix, String split) {

        StringBuilder sbd = new StringBuilder(prefix);

        for (Object obj : value) {

            sbd.append(format(obj)).append(split);
        }

        return sbd.substring(0, sbd.length() - 1) + suffix;
    }


    private String format(Object value) {

        StringBuilder sbd = new StringBuilder();
        if (value instanceof String) {

            sbd.append("'").append(value).append("'");
        } else if (value instanceof Date) {

            if (dialect.equals("mysql")) {

                sbd.append("date_format('").append(DateUtil.dateToString((Date) value)).append("' , '%Y-%m-%d %H:%i:%s')");
            } else if (dialect.equals("oracle")) {

                sbd.append("to_date('").append(DateUtil.dateToString((Date) value)).append("' , 'yyyy-mm-dd hh24:mi:ss')");
            }
        }else {

            return value == null? null: value.toString();
        }

        return sbd.toString();
    }


    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.baseSqlId = properties.getProperty(Constant.Crud.BASE_SQL_ID);
        this.dialect = properties.getProperty(Constant.Crud.DIALECT);
    }
}

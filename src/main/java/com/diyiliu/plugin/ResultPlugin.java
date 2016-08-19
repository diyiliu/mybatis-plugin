package com.diyiliu.plugin;

import com.diyiliu.bll.bean.BaseEntity;
import com.diyiliu.other.Constant;
import com.diyiliu.plugin.abs.SPlugin;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * Description: ResultPlugin
 * Author: DIYILIU
 * Update: 2016-07-11 15:56
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class ResultPlugin extends SPlugin {

    private String resultSqlId;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        strip(invocation.getTarget());

        MappedStatement mappedStatement = (MappedStatement) getValue("mappedStatement");

        String sqlId = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
        if (sqlId.matches(resultSqlId)) {
            BoundSql boundSql = (BoundSql) getValue("boundSql");
            Object parameterObject = boundSql.getParameterObject();

            if (parameterObject instanceof BaseEntity) {
                BaseEntity entity = (BaseEntity) parameterObject;

                Statement statement = (Statement) invocation.getArgs()[0];
                ResultSet resultSet = statement.getResultSet();

                List results = entity.toEntity(resultSet);

                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }

                return results;
            }
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是ResultSetHandler类型时，才包装目标类，否者直接返回目标本身,减少目标被代理的次数
        if (target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        this.resultSqlId = properties.getProperty(Constant.Crud.RESULT_SQL_ID);
    }
}

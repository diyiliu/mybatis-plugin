package com.diyiliu.plugin;

import com.diyiliu.other.Constant;
import com.diyiliu.other.Pagination;
import com.diyiliu.other.PaginationHelper;
import com.diyiliu.plugin.abs.SPlugin;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Description: PaginationPlugin
 * Author: DIYILIU
 * Update: 2015-11-13 14:32
 */

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PaginationPlugin extends SPlugin {

    private String pageSqlId; // mybaits的数据库xml映射文件中需要拦截的ID(正则匹配)
    private String dialect; // 数据库方言

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        strip(invocation.getTarget());

        MappedStatement mappedStatement = (MappedStatement) getValue("mappedStatement");

        String sqlId = mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);

        Pagination pagination = PaginationHelper.getPagination();
        if (sqlId.matches(pageSqlId) &&
                pagination != null && dialect != null) {
            logger.info("Mybatis 分页插件...");
            // 数据源
            DataSource dataSource = (DataSource)getValue("configuration.environment.dataSource");

            BoundSql boundSql = (BoundSql) getValue("boundSql");
            String sql = boundSql.getSql();

            // 设置参数(切记要及时关闭数据库连接connection!)
            setPageParameter(sql, dataSource, mappedStatement, boundSql, pagination);

            // 分页sql
            sql = buidPageSql(sql, pagination.getOffset(), pagination.getLimit());
            // 重写sql
            setValue("boundSql.sql", sql);
        }

        return invocation.proceed();
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

    public String buidPageSql(String sql, int offset, int limit) {

        if (dialect.equals("mysql")) {

            return buildPageSqlForMysql(sql, offset, limit).toString();
        }

        if (dialect.equals("oracle")) {

            return buildPageSqlForOracle(sql, offset, limit).toString();
        }

        return null;
    }


    public StringBuilder buildPageSqlForMysql(String sql, int offset, int limit) {
        StringBuilder pageSql = new StringBuilder(sql);
        pageSql.append(" limit " + offset + "," + limit);
        return pageSql;
    }

    public StringBuilder buildPageSqlForOracle(String sql, int offset, int limit) {
        StringBuilder pageSql = new StringBuilder(sql);
        pageSql.append("select * from ( select temp.*, rownum row_id from ( ");
        pageSql.append(sql);
        pageSql.append(" ) temp where rownum <= ").append(offset + limit);
        pageSql.append(") where row_id > ").append(offset);
        return pageSql;
    }

    /**
     * 从数据库里查询总的记录数并计算总页数，回写进分页参数
     *
     * @param sql
     * @param dataSource
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, DataSource dataSource, MappedStatement mappedStatement,
                                  BoundSql boundSql, Pagination page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") as total";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            setParameters(statement, mappedStatement, countBS, boundSql.getParameterObject());
            rs = statement.executeQuery();
            long count = 0;
            if (rs.next()) {
                count = rs.getLong(1);
            }
            page.setTotal(count);
        } catch (SQLException e) {
            logger.error("SQLException", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                logger.error("SQLException", e);
            }
        }
    }

    /**
     * 对SQL参数(?)设值
     *
     * @param preparedStatement
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws SQLException
     */
    private void setParameters(PreparedStatement preparedStatement, MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(preparedStatement);
    }

    @Override
    public void setProperties(Properties properties) {
        this.pageSqlId = properties.getProperty(Constant.Crud.PAGE_SQL_ID);
        this.dialect = properties.getProperty(Constant.Crud.DIALECT);
    }
}

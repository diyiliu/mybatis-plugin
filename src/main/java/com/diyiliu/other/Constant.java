package com.diyiliu.other;

/**
 * Description: Constant
 * Author: DIYILIU
 * Update: 2016-02-19 14:40
 */
public class Constant {

    /**
     * 数据库操作
     */
    public enum Crud{
        ;
        public final static String INSERT = "insertEntity";
        public final static String DELETE = "deleteEntity";
        public final static String UPDATE = "updateEntity";
        public final static String SELECT = "selectEntity";

        public final static String DIALECT = "dialect";
        public final static String BASE_SQL_ID = "baseSqlId";
        public final static String PAGE_SQL_ID = "pageSqlId";
        public final static String RESULT_SQL_ID = "resultSqlId";
    }
}

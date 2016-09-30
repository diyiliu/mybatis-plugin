package com.diyiliu.other;

/**
 * Description: Constant
 * Author: DIYILIU
 * Update: 2016-02-19 14:40
 */
public class Constant {

    /**
     * 数据库操作（增、删、改、查）
     */
    public enum Crud{
        ;
        public final static String INSERT = "insertEntity";
        public final static String DELETE = "deleteEntity";
        public final static String UPDATE = "updateEntity";
        public final static String SELECT = "selectEntity";
        public final static String SELECT_LIST = "selectEntityForList";

        public final static String DIALECT = "dialect";
        public final static String BASE_SQL_ID = "baseSqlId";
        public final static String PAGE_SQL_ID = "pageSqlId";
        public final static String RESULT_SQL_ID = "resultSqlId";
    }


    /**
     * 条件查询
     */
    public enum QBuilder{
        ;
        public final static String IS_NULL = "isNull";
        public final static String IS_NOT_NULL = "isNotNull";
        public final static String EQUAL = "equal";
        public final static String NOT_EQUAL = "notEqual";
        public final static String GREATER = "greater";
        public final static String GREATER_OR_EQUAL = "greaterOrEqual";
        public final static String LESS = "less";
        public final static String LESS_OR_EQUAL = "lessOrEqual";
        public final static String IN = "in";
        public final static String NOT_IN = "notIn";
        public final static String ORDER_BY = "orderBy";
        // 递归 value
        public final static String RECURSIVE = "recursive";
        public final static String AUTO = "auto";

    }
}

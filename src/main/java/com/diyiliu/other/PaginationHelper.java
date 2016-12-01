package com.diyiliu.other;

/**
 * Description: PaginationHelper
 * Author: DIYILIU
 * Update: 2015-11-20 16:16
 */
public class PaginationHelper {

    private static ThreadLocal<Pagination> LOCAL_PAGINATION = new ThreadLocal<Pagination>();

    public static void page(int page, int limit) {
        Pagination pagination = LOCAL_PAGINATION.get();
        if (pagination == null) {
            pagination = new Pagination();
        }
        pagination.setOffset((page > 1 ? page - 1 : 0) * limit);
        pagination.setLimit(limit);

        LOCAL_PAGINATION.set(pagination);
    }

    public static Pagination getPagination() {

        return LOCAL_PAGINATION.get();
    }

    public static void clear() {

        LOCAL_PAGINATION.remove();
    }

    public static long getTotal() {

        return LOCAL_PAGINATION.get().getTotal();
    }
}

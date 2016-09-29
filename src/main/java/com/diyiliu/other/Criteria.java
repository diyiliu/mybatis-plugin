package com.diyiliu.other;

/**
 * Description: Criteria
 * Author: DIYILIU
 * Update: 2016-09-29 09:13
 */

public class Criteria {

    private String symbol;
    private String key;
    private Object[] value;

    public Criteria() {
    }

    public Criteria(String key, Object[] value) {
        this.key = key;
        this.value = value;
    }

    public Criteria(String symbol, String key, Object[] value) {
        this.symbol = symbol;
        this.key = key;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object[] getValue() {
        return value;
    }

    public void setValue(Object[] value) {
        this.value = value;
    }
}

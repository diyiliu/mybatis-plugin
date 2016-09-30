package com.diyiliu.other;

/**
 * Description: Criteria
 * Author: DIYILIU
 * Update: 2016-09-29 09:13
 */

public class Criteria<T> {

    private String symbol;
    private String key;
    private T[] value;

    private Boolean or = Boolean.FALSE;

    public Criteria() {
    }

    public Criteria(String key, T[] value) {
        this.key = key;
        this.value = value;
    }

    public Criteria(String symbol, String key, T[] value) {
        this.symbol = symbol;
        this.key = key;
        this.value = value;
    }

    public Criteria(String symbol, String key, T[] value, Boolean or) {
        this.symbol = symbol;
        this.key = key;
        this.value = value;
        this.or = or;
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

    public T[] getValue() {
        return value;
    }

    public void setValue(T[] value) {
        this.value = value;
    }

    public Boolean getOr() {
        return or;
    }

    public void setOr(Boolean or) {
        this.or = or;
    }
}

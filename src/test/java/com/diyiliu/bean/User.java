package com.diyiliu.bean;

import com.diyiliu.bll.bean.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Description: User
 * Author: DIYILIU
 * Update: 2016-08-19 15:21
 */

@Table(name = "USER", schema = "ID")
public class User extends BaseEntity {

    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AGE")
    private Integer age;

    public User() {
    }

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
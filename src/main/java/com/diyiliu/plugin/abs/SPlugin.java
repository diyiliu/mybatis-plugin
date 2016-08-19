package com.diyiliu.plugin.abs;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: SPlugin
 * Author: DIYILIU
 * Update: 2016-07-11 16:03
 */

public abstract  class SPlugin implements Interceptor{
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private final static ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private final static ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    protected  MetaObject metaStatementHandler;

    /**
     * 剥离代理对象
     *
     * @param source
     * @return
     */
    protected void strip(Object source){
        MetaObject metaStatementHandler = MetaObject.forObject(source, DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");

            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        //
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        //
        while (metaStatementHandler.hasGetter("delegate")) {
            Object object = metaStatementHandler.getValue("delegate");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY,
                    DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        this.metaStatementHandler = metaStatementHandler;
    }

    protected Object getValue(String name){

        return  metaStatementHandler.getValue(name);
    }

    protected void setValue(String name, Object value){

        metaStatementHandler.setValue(name, value);
    }
}

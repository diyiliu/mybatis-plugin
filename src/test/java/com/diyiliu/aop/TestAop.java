package com.diyiliu.aop;

import com.diyiliu.bean.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

/**
 * Description: TestAop
 * Author: DIYILIU
 * Update: 2016-10-20 14:50
 */

@Aspect
@Service
public class TestAop {

    @Pointcut(value = "execution(* com..*.service.*.insert(..)) && args(obj)", argNames = "obj")
    public void beforePointcut(Object obj) {
    }

    @Before(value = "beforePointcut(obj)", argNames = "obj")
    public void beforeAdvice(Object obj) {
        User user = (User) obj;
        System.out.println("beforeAdvice:" + user.getName());
    }


    @AfterReturning(value = "execution(* com..*.service.*.insert(..)) && args(obj))")
    public void insertServiceDo(JoinPoint joinPoint, Object obj) {
        User entity = (User) obj;

        System.out.println("afterAdvice:" + entity.getName());
    }


}

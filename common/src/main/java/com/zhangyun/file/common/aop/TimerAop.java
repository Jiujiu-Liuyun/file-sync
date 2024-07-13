package com.zhangyun.file.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Aspect //必须的注解
@Order(-1)
@Slf4j
public class TimerAop {

    //在带有AopLog注解的方法进行切入（注:此处的 * *前面都要有一个空格）
    @Pointcut("execution(@com.zhangyun.file.common.annotation.Timer * *(..))")
    public void pointcut(){
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object obj = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("方法【{}】耗时{}S", joinPoint.getSignature().getName(), (end - start) / 1000.0);
        return obj;
    }

}

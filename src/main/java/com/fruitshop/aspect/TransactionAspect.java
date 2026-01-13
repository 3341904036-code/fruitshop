package com.fruitshop.aspect;

import lombok.extern.slf4j. Slf4j;
import org. aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation. Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 事务切面
 */
@Slf4j
@Aspect
@Component
public class TransactionAspect {

    /**
     * 在service方法前打印事务开始日志
     */
    @Before("execution(public * com.fruitshop.service.impl..*(..))")
    public void beforeTransaction(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.debug("事务开始: {}.{}", className, methodName);
    }
}
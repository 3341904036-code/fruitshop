package com.fruitshop. aspect;

import lombok.extern.slf4j. Slf4j;
import org. aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang. annotation.*;
import org.springframework.stereotype. Component;
import java.util.Arrays;

/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 定义切点：所有controller方法
     */
    @Pointcut("execution(public * com.fruitshop.controller..*(..))")
    public void controllerPointcut() {
    }

    /**
     * 定义切点：所有service方法
     */
    @Pointcut("execution(public * com.fruitshop.service..*(..))")
    public void servicePointcut() {
    }

    /**
     * 前置通知
     */
    @Before("controllerPointcut() || servicePointcut()")
    public void beforeAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        log.info("==> 执行方法:  {}. {}, 参��: {}", className, methodName, Arrays.toString(args));
    }

    /**
     * 后置返回通知
     */
    @AfterReturning(pointcut = "controllerPointcut() || servicePointcut()", returning = "result")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.info("<== 方法执行完成: {}.{}, 返回值: {}", className, methodName, result);
    }

    /**
     * 环绕通知
     */
    @Around("controllerPointcut() || servicePointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint. getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.info("方法执行耗时: {}. {} - {}ms", className, methodName, duration);

            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            log.error("方法执行异常:  {}.{} - {}ms, 异常信息: {}", className, methodName, duration, e.getMessage());

            throw e;
        }
    }

    /**
     * 异常通知
     */
    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut()", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        log.error("方法执行异常: {}.{}, 异常类型: {}, 异常信息: {}",
                className, methodName, ex.getClass().getName(), ex.getMessage());
    }
}
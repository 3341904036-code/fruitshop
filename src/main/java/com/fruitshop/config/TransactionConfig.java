package com.fruitshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation. EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务配置类
 */
@Configuration
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class TransactionConfig {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    /**
     * TransactionTemplate Bean
     */
    @Bean
    public TransactionTemplate transactionTemplate() {
        return new TransactionTemplate(platformTransactionManager);
    }
}
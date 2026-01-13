package com.fruitshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration. class,
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@MapperScan("com.fruitshop.dao. mapper")
@ComponentScan(basePackages = {
        "com.fruitshop.controller",
        "com.fruitshop.service",
        "com.fruitshop.config",
        "com.fruitshop.aspect",
        "com.fruitshop.listener",
        "com.fruitshop.exception"
})
@EnableTransactionManagement
@EnableAspectJAutoProxy(exposeProxy = true)
public class FruitshopApplication {

    public static void main(String[] args) {
        SpringApplication. run(FruitshopApplication.class, args);

        System.out.println("\n╔════════════════════════════════════════════════════════╗");
        System.out. println("║   🍎 水果商店管理系统 - Fruit Shop Management System 🍎   ║");
        System.out.println("║                                                        ║");
        System.out.println("║                   服务已启动，访问地址:                    ║");
        System.out.println("║          http://localhost:8080/fruitshop/login.html    ║");
        System.out.println("║                                                        ║");
        System.out.println("║              默认账号:                                   ║");
        System.out.println("║              - 用户ID: 10000                            ║");
        System.out.println("║              - 密码: 123456                             ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝\n");
    }
}
package com.fruitshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 页面跳转控制器（专门处理页面跳转请求）
 */
@Controller
public class PageController {

    // 1. 跳转到登录页（访问 /login 时跳转至 templates/login.html）
    @GetMapping("/login")
    public String toLogin() {
        // 返回的字符串是「视图名」，结合yml中的前缀/后缀，最终映射到 templates/login.html
        return "login";
    }

    // 2. 跳转到首页（访问 /index 或 / 时跳转至 templates/index.html）
    @GetMapping({"/index", "/"})
    public String toIndex() {
        return "index";
    }

    // 3. 跳转到顾客管理页面（访问 /customer/list 时跳转至 templates/customer/list.html）
    @GetMapping("/customer/list")
    public String toCustomerList() {
        // 对应 templates/customer/ 下的 list.html（需确保该文件存在）
        return "customer/list";
    }

    // 4. 跳转到水果管理页面
    @GetMapping("/fruit/list")
    public String toFruitList() {
        return "fruit/list";
    }

    // 5. 跳转到订单管理页面
    @GetMapping("/order/list")
    public String toOrderList() {
        return "order/list";
    }

}
package com.fruitshop.controller;

import lombok.extern.slf4j. Slf4j;
import org. springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 视图控制器 - 处理HTML模板视图
 */
@Slf4j
@Controller
public class ViewController {

    /**
     * 登录页面
     * GET /login.html
     */
    @GetMapping("/login.html")
    public String login() {
        log.debug("访问登录页面 /login.html");
        return "login";  // 返回 templates/login.html
    }

    /**
     * 首页
     * GET /index.html
     */
    @GetMapping("/index.html")
    public String index() {
        log.debug("访问首页 /index.html");
        return "index";  // 返回 templates/index.html
    }

    /**
     * 重定向根路径到登录页面
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login.html";
    }
}
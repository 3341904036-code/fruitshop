package com.fruitshop.controller;

import com.fruitshop.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String toIndex(Model model) {
        addUserToModel(model);
        return "index";
    }

    // 3. 跳转到顾客管理页面（访问 /customer/list 时跳转至 templates/customer/list.html）
    @GetMapping("/customer/list")
    public String toCustomerList(Model model) {
        addUserToModel(model);
        // 对应 templates/customer/ 下的 list.html（需确保该文件存在）
        return "customer/list";
    }

    // 4. 跳转到水果管理页面
    @GetMapping("/fruit/list")
    public String toFruitList(Model model) {
        addUserToModel(model);
        return "fruit/list";
    }

    // 5. 跳转到订单管理页面
    @GetMapping("/order/list")
    public String toOrderList(Model model) {
        addUserToModel(model);
        return "order/list";
    }

    // 6. 跳转到供应商管理页面
    @GetMapping("/supplier/list")
    public String toSupplierList(Model model) {
        addUserToModel(model);
        return "supplier/list";
    }

    // 7. 跳转到水果添加页面
    @GetMapping("/fruit/add")
    public String toFruitAdd(Model model) {
        addUserToModel(model);
        return "fruit/add";
    }

    // 8. 跳转到顾客添加页面
    @GetMapping("/customer/add")
    public String toCustomerAdd(Model model) {
        addUserToModel(model);
        return "customer/add";
    }

    // 9. 跳转到订单添加页面
    @GetMapping("/order/add")
    public String toOrderAdd(Model model) {
        addUserToModel(model);
        return "order/add";
    }

    // 10. 跳转到供应商添加页面
    @GetMapping("/supplier/add")
    public String toSupplierAdd(Model model) {
        addUserToModel(model);
        return "supplier/add";
    }

    // 11. 跳转到数据导出页面
    @GetMapping("/dashboard/export")
    public String toExport(Model model) {
        addUserToModel(model);
        return "dashboard/export";
    }

    /**
     * 将当前登录用户信息添加到模型中
     */
    private void addUserToModel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !"anonymousUser".equals(authentication.getPrincipal())) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User loginUser = (User) principal;
                model.addAttribute("loginUser", loginUser);
            }
        }
    }
}
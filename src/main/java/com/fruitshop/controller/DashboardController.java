package com.fruitshop.controller;

import com.fruitshop.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 仪表板控制器 - 提供统计数据分析接口
 */
@Slf4j
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private FruitService fruitService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private VipService vipService;

    /**
     * 获取仪表板统计数据
     * GET /api/dashboard/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        log.debug("请求仪表板统计数据");

        Map<String, Object> stats = new HashMap<>();

        try {
            // 总顾客数
            int customerCount = customerService.count();
            stats.put("customerCount", customerCount);

            // 水果种类数
            int fruitCount = fruitService.count();
            stats.put("fruitCount", fruitCount);

            // 总订单数
            int orderCount = orderService.count();
            stats.put("orderCount", orderCount);

            // VIP顾客数
            int vipCount = vipService.getVipCustomerCount();
            stats.put("vipCount", vipCount);

            log.debug("仪表板统计数据: {}", stats);

            // 替换 Java 9+ 的 Map.of()，改用 Java 8 兼容的 HashMap
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("code", 200);
            successResponse.put("message", "获取统计数据成功");
            successResponse.put("data", stats);
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            log.error("获取仪表板统计数据失败", e);
            stats.put("customerCount", 0);
            stats.put("fruitCount", 0);
            stats.put("orderCount", 0);
            stats.put("vipCount", 0);

            // 同样替换 Map.of() 为 HashMap
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 500);
            errorResponse.put("message", "获取统计数据失败: " + e.getMessage());
            errorResponse.put("data", stats);
            return ResponseEntity.ok(errorResponse);
        }
    }
}
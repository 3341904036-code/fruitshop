package com.fruitshop.controller;

import com.fruitshop.dto.OrderDTO;
import com.fruitshop.exception.BusinessException;
import com. fruitshop.service.OrderService;
import com.fruitshop.util.ResponseUtil;
import com.fruitshop.vo.OrderVO;
import lombok. extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation. Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public ResponseUtil<?> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            log. info("创建订单请求: cId={}", orderDTO.getCId());

            Integer oNum = orderService.createOrder(orderDTO);

            log.info("订单创建成功: oNum={}", oNum);
            return ResponseUtil.success("订单创建成功", oNum);
        } catch (BusinessException e) {
            log.error("创建订单失败: {}", e.getMessage());
            return ResponseUtil.error(400, e. getMessage());
        } catch (Exception e) {
            log.error("创建订单异常", e);
            return ResponseUtil. error("创建订单异常");
        }
    }

    /**
     * 修改订单
     */
    @PutMapping("/update")
    public ResponseUtil<?> updateOrder(@RequestBody OrderDTO orderDTO) {
        try {
            log.info("修改订单请求: oNum={}", orderDTO.getONum());

            boolean result = orderService.updateOrder(orderDTO);

            if (result) {
                log.info("订单修改成功:  oNum={}", orderDTO.getONum());
                return ResponseUtil.success("修改成功");
            } else {
                return ResponseUtil.error("修改失败");
            }
        } catch (BusinessException e) {
            log.error("修改订单失败:  {}", e.getMessage());
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("修改订单异常", e);
            return ResponseUtil.error("修改订单异常");
        }
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/delete/{oNum}")
    public ResponseUtil<?> deleteOrder(@PathVariable Integer oNum) {
        try {
            log.info("删除订单请求: oNum={}", oNum);

            boolean result = orderService.deleteOrder(oNum);

            if (result) {
                log.info("订单删除成功:  oNum={}", oNum);
                return ResponseUtil.success("删除成功");
            } else {
                return ResponseUtil.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除订单异常", e);
            return ResponseUtil.error("删除订单异常");
        }
    }

    /**
     * 查询单个订单
     */
    @GetMapping("/{oNum}")
    public ResponseUtil<?> getOrder(@PathVariable Integer oNum) {
        try {
            log.info("查询订单请求: oNum={}", oNum);

            OrderVO order = orderService.getOrderById(oNum);

            if (order != null) {
                return ResponseUtil.success("查询成功", order);
            } else {
                return ResponseUtil. error(404, "订单不存在");
            }
        } catch (Exception e) {
            log.error("查询订单异常", e);
            return ResponseUtil.error("查询订单异常");
        }
    }

    /**
     * 查询所有订单
     */
    @GetMapping("/list/all")
    public ResponseUtil<?> getAllOrders() {
        try {
            log.info("查询所有订单");

            List<OrderVO> orders = orderService. getAllOrders();

            return ResponseUtil.success("查询成功", orders);
        } catch (Exception e) {
            log.error("查询订单列表异常", e);
            return ResponseUtil.error("查询订单列表异常");
        }
    }

    /**
     * 根据顾客查询订单
     */
    @GetMapping("/list/customer/{cId}")
    public ResponseUtil<?> getOrdersByCustomer(@PathVariable Integer cId) {
        try {
            log.info("根据顾客查询订单: cId={}", cId);

            List<OrderVO> orders = orderService.getOrdersByCustomer(cId);

            return ResponseUtil.success("查询成功", orders);
        } catch (Exception e) {
            log.error("根据顾客查询订单异常", e);
            return ResponseUtil.error("根据顾客查询订单异常");
        }
    }

    /**
     * 计算订单总额
     */
    @PostMapping("/calculateTotal/{oNum}")
    public ResponseUtil<?> calculateTotal(@PathVariable Integer oNum) {
        try {
            log.info("计算订单总额:  oNum={}", oNum);

            orderService.calculateOrderTotal(oNum);

            log.info("订单总额计算成功: oNum={}", oNum);
            return ResponseUtil.success("计算成功");
        } catch (Exception e) {
            log.error("计算订单总额异常", e);
            return ResponseUtil. error("计算订单总额异常");
        }
    }
}
package com. fruitshop.service;

import com.fruitshop. dto.OrderDTO;
import com. fruitshop.vo.OrderVO;
import java. util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    Integer createOrder(OrderDTO orderDTO);

    /**
     * 修改订单
     */
    boolean updateOrder(OrderDTO orderDTO);

    /**
     * 删除订单
     */
    boolean deleteOrder(Integer oNum);

    /**
     * 查询单个订单
     */
    OrderVO getOrderById(Integer oNum);

    /**
     * 查询所有订单
     */
    List<OrderVO> getAllOrders();

    /**
     * 根据顾客查询订单
     */
    List<OrderVO> getOrdersByCustomer(Integer cId);

    /**
     * 计算订单总额
     */
    void calculateOrderTotal(Integer oNum);

    int count();
}
package com.fruitshop.dao.mapper;

import com.baomidou. mybatisplus.core.mapper.BaseMapper;
import com. fruitshop.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis. annotations.Select;
import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 根据顾客ID查询订单
     */
    @Select("SELECT * FROM orders WHERE c_id = #{cId} ORDER BY o_date DESC")
    List<Order> selectByCustomerId(Integer cId);

    /**
     * 查询所有订单（按日期倒序）
     */
    @Select("SELECT * FROM orders ORDER BY o_date DESC")
    List<Order> selectAllOrders();

    /**
     * 查询VIP顾客的订单总额
     */
    @Select("SELECT SUM(pay) FROM orders WHERE c_id = #{cId}")
    Double selectTotalSpentByCustomer(Integer cId);
}
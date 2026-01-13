package com.fruitshop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis. annotations.Delete;
import java.util.List;

/**
 * 订单项Mapper接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    /**
     * 根据订单号查询订单项
     */
    @Select("SELECT * FROM orderitems WHERE o_num = #{oNum} ORDER BY o_item")
    List<OrderItem> selectByOrderNum(Integer oNum);

    /**
     * 根据水果ID查询订单项
     */
    @Select("SELECT * FROM orderitems WHERE f_id = #{fId} ORDER BY o_num DESC")
    List<OrderItem> selectByFruitId(String fId);

    /**
     * 删除订单的所有项
     */
    @Delete("DELETE FROM orderitems WHERE o_num = #{oNum}")
    int deleteByOrderNum(Integer oNum);

    /**
     * 查询订单项总数
     */
    @Select("SELECT COUNT(*) FROM orderitems WHERE o_num = #{oNum}")
    int countByOrderNum(Integer oNum);
}
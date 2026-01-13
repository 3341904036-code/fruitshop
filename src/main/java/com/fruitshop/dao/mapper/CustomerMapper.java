package com.fruitshop.dao. mapper;

import com.baomidou.mybatisplus. core.mapper.BaseMapper;
import com.fruitshop. entity.Customer;
import org. apache.ibatis.annotations. Mapper;
import org.apache. ibatis.annotations.Select;
import java.math.BigDecimal;
import java.util.List;

/**
 * 顾客Mapper接口
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

    /**
     * 根据VIP等级查询顾客
     */
    @Select("SELECT * FROM customers WHERE c_vip = #{vipLevel} ORDER BY c_id")
    List<Customer> selectByVipLevel(Integer vipLevel);

    /**
     * 查询所有VIP顾客
     */
    @Select("SELECT * FROM customers WHERE c_vip > 0 ORDER BY c_vip DESC, c_id")
    List<Customer> selectAllVipCustomers();

    /**
     * 根据城市查询顾客
     */
    @Select("SELECT * FROM customers WHERE c_city = #{city} ORDER BY c_id")
    List<Customer> selectByCity(String city);
}
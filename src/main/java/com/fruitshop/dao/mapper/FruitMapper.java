package com.fruitshop. dao.mapper;

import com. baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.Fruit;
import org.apache.ibatis. annotations.Mapper;
import org.apache.ibatis.annotations. Select;
import org.apache.ibatis.annotations. Update;
import java.util. List;

/**
 * 水果Mapper接口
 */
@Mapper
public interface FruitMapper extends BaseMapper<Fruit> {

    /**
     * 根据供应商ID查询水果
     */
    @Select("SELECT * FROM fruits WHERE s_id = #{sId} ORDER BY f_id")
    List<Fruit> selectBySupplier(Integer sId);

    /**
     * 根据名称模糊查询水果
     */
    @Select("SELECT * FROM fruits WHERE f_name LIKE CONCAT('%', #{name}, '%') ORDER BY f_id")
    List<Fruit> selectByNameLike(String name);

    /**
     * 查询库存不足的水果
     */
    @Select("SELECT * FROM fruits WHERE quantity < #{minQty} ORDER BY quantity")
    List<Fruit> selectLowStock(Integer minQty);

    /**
     * 更新水果库存
     */
    @Update("UPDATE fruits SET quantity = quantity - #{reduceQty} WHERE f_id = #{fId}")
    int updateQuantity(String fId, Integer reduceQty);
}
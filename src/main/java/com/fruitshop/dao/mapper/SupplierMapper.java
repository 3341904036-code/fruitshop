package com.fruitshop.dao.mapper;

import com.baomidou.mybatisplus.core. mapper.BaseMapper;
import com.fruitshop.entity. Supplier;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 供应商Mapper接口
 */
@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {

    /**
     * 根据城市查询供应商
     */
    @Select("SELECT * FROM suppliers WHERE s_city = #{city} ORDER BY s_id")
    List<Supplier> selectByCity(String city);

    /**
     * 查询所有供应商
     */
    @Select("SELECT * FROM suppliers ORDER BY s_id")
    List<Supplier> selectAllSuppliers();
}
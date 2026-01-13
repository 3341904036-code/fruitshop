package com.fruitshop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper. BaseMapper;
import com.fruitshop.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 角色Mapper接口
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色名称查询角色
     */
    @Select("SELECT * FROM roles WHERE role_name = #{roleName}")
    Role selectByRoleName(String roleName);
}
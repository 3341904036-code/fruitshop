package com.fruitshop.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fruitshop.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户ID查询用户
     */
    @Select("SELECT * FROM user WHERE u_id = #{uId}")
    User selectByUId(Integer uId);

    /**
     * 根据角色ID查询用户列表
     */
    @Select("SELECT * FROM user WHERE role_id = #{roleId}")
    List<User> selectByRoleId(Integer roleId);

    /**
     * 查询所有用户
     */
    @Select("SELECT * FROM user ORDER BY id DESC")
    List<User> selectAllUsers();
}
package com.fruitshop.service;

import com.fruitshop.dto.LoginDTO;
import com. fruitshop.entity.User;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    User login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    boolean register(Integer uId, String password, String remark, Integer roleId);

    /**
     * 修改密码
     */
    boolean changePassword(Integer uId, String oldPassword, String newPassword);

    /**
     * 用户注销
     */
    boolean logout(Integer uId);

    /**
     * 验证密码
     */
    boolean validatePassword(String plainPassword, String encryptedPassword);

    /**
     * 根据用户ID查询用户
     */
    User getUserById(Integer uId);
}
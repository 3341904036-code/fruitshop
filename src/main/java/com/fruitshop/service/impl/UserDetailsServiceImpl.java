package com.fruitshop.service.impl;

import com.fruitshop.dao.mapper.UserMapper;
import com.fruitshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义UserDetailsService实现
 * 用于从数据库加载用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 将用户名转换为整数类型的uId
        Integer uId;
        try {
            uId = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("用户名格式错误: " + username);
        }

        // 通过UserMapper查询用户
        User user = userMapper.selectByUId(uId);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 检查用户状态
        new AccountStatusUserDetailsChecker().check(user);

        // 返回用户信息，User类已经实现了UserDetails接口
        return user;
    }
}
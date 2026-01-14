package com.fruitshop.config;

import com.fruitshop.dao.mapper.UserMapper;
import com.fruitshop.entity.User;
import com.fruitshop.util.AESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 自定义认证提供程序，用于处理AES加密密码的验证
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 将用户名转换为整数类型的uId
        Integer uId;
        try {
            uId = Integer.parseInt(username);
        } catch (NumberFormatException e) {
            throw new BadCredentialsException("用户名格式错误");
        }

        // 查询用户
        User user = userMapper.selectByUId(uId);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 验证密码 - 使用AES加密方式：uId + password
        String plainPassword = uId + password;
        if (!AESUtil.validatePassword(plainPassword, user.getPwd())) {
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 使用完整的User对象作为principal，这样可以保留所有用户信息
        return new UsernamePasswordAuthenticationToken(
            user, 
            password, // 原始密码，不再需要加密版本
            user.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
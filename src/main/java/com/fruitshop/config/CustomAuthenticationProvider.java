package com.fruitshop.config;

import com.fruitshop.dao.mapper.UserMapper;
import com.fruitshop.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 自定义认证提供程序，用于处理AES加密密码的验证
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    // 添加日志记录器（推荐使用slf4j，而非System.out）
    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AESPasswordEncoder aesPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 获取前端提交的参数（重点：确认前端参数名是username，否则修改这里！）
        String inputParam = authentication.getName(); // 前端传的参数名如果是uid，需先在SecurityConfig中配置usernameParameter("uid")
        String rawPassword = authentication.getCredentials().toString();

        log.info("开始登录认证 - 前端输入参数: {}, 密码(脱敏): {}", inputParam, rawPassword.replaceAll(".", "*"));

        // 2. 转换用户ID并处理异常（添加日志）
        Integer uId;
        try {
            uId = Integer.parseInt(inputParam);
            log.info("用户ID转换成功 - uId: {}", uId);
        } catch (NumberFormatException e) {
            log.error("用户名格式错误 - 输入的不是数字: {}", inputParam, e);
            throw new BadCredentialsException("用户名格式错误，请输入数字类型的用户ID");
        }

        // 3. 查询用户（添加日志）
        User user = userMapper.selectByUId(uId);
        if (user == null) {
            log.error("登录失败 - 用户不存在: uId={}", uId);
            throw new UsernameNotFoundException("用户不存在: " + uId);
        }
        log.info("查询到用户信息 - uId: {}, 用户名: {}", uId, user.getUsername()); // 假设User有username字段

        // 4. 验证密码（添加日志）
        String dbEncryptedPwd = user.getPwd();
        boolean passwordMatch = aesPasswordEncoder.matches(uId, rawPassword, dbEncryptedPwd);
        log.info("密码验证结果 - uId: {}, 拼接明文: {}+{}, 数据库加密密码: {}, 匹配结果: {}",
                uId, uId, rawPassword.replaceAll(".", "*"), dbEncryptedPwd, passwordMatch);

        if (!passwordMatch) {
            log.error("登录失败 - 密码不匹配: uId={}", uId);
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 5. 处理用户权限（避免null，兜底空权限集合）
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            log.warn("用户无权限配置 - uId: {}, 分配默认空权限", uId);
            authorities = AuthorityUtils.NO_AUTHORITIES; // Spring Security内置空权限集合
        }

        // 6. 认证成功，返回令牌（密码建议存加密后的，而非原始密码）
        log.info("登录认证成功 - uId: {}", uId);
        return new UsernamePasswordAuthenticationToken(
                user,          // 认证主体（用户信息）
                dbEncryptedPwd,// 存储加密后的密码（而非原始密码，更安全）
                authorities    // 用户权限
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
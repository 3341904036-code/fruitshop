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
 * 自定义认证提供程序，用于处理AES加密密码的验证（修复版）
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AESPasswordEncoder aesPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 1. 获取前端提交的参数（SecurityConfig中已配置usernameParameter("uId")）
        String inputParam = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        log.info("开始登录认证 - 前端输入参数: {}, 密码(脱敏): {}",
                inputParam == null ? "null" : inputParam,
                rawPassword.replaceAll(".", "*"));

        // ========== 关键修复1：增加参数非空+空白校验 ==========
        if (inputParam == null || inputParam.trim().isEmpty()) {
            log.error("登录失败 - 用户ID为空");
            throw new BadCredentialsException("用户ID不能为空");
        }
        // 去除首尾空白字符，避免空格导致的转换异常
        String cleanUIdStr = inputParam.trim();

        // 2. 转换用户ID并处理异常（优化：使用cleanUIdStr）
        Integer uId;
        try {
            uId = Integer.parseInt(cleanUIdStr);
            log.info("用户ID转换成功 - uId: {}", uId);
        } catch (NumberFormatException e) {
            log.error("用户名格式错误 - 输入的不是有效数字: {}", cleanUIdStr, e);
            throw new BadCredentialsException("用户ID格式错误，请输入纯数字（如：10017）");
        }

        // 3. 查询用户（逻辑不变，增强日志）
        User user = userMapper.selectByUId(uId);
        if (user == null) {
            log.error("登录失败 - 用户不存在: uId={}", uId);
            throw new UsernameNotFoundException("用户不存在: " + uId);
        }
        log.info("查询到用户信息 - uId: {}, 角色名: {}", uId, user.getRoleName());

        // 4. 验证密码（逻辑不变）
        String dbEncryptedPwd = user.getPwd();
        boolean passwordMatch = aesPasswordEncoder.matches(uId, rawPassword, dbEncryptedPwd);
        log.info("密码验证结果 - uId: {}, 数据库加密密码: {}, 匹配结果: {}",
                uId, dbEncryptedPwd, passwordMatch);

        if (!passwordMatch) {
            log.error("登录失败 - 密码不匹配: uId={}", uId);
            throw new BadCredentialsException("用户名或密码错误");
        }

        // 5. 处理用户权限（逻辑不变）
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        if (authorities == null || authorities.isEmpty()) {
            log.warn("用户无权限配置 - uId: {}, 分配默认空权限", uId);
            authorities = AuthorityUtils.NO_AUTHORITIES;
        }

        // 6. 认证成功，返回令牌（优化：密码存null更安全，避免敏感信息存储）
        log.info("登录认证成功 - uId: {}", uId);
        return new UsernamePasswordAuthenticationToken(
                user,          // 认证主体（用户信息）
                null,          // 密码字段存null，避免存储加密密码（SecurityContext中无需存密码）
                authorities    // 用户权限
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
package com.fruitshop.config;

import com.fruitshop.util.AESUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * AES密码编码器
 * 用于处理与数据库中AES加密密码的匹配
 */
public class AESPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        // 这里不应该直接调用，因为密码已经在业务层加密过了
        // 在实际应用中，用户输入的密码格式应该是"uId+rawPassword"
        throw new UnsupportedOperationException("AES加密应该在业务层完成");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 这里我们无法知道uId，因此需要特殊处理
        // 实际上，我们会将rawPassword和encodedPassword传递给UserDetails实现
        // 并在其中处理uId+password的组合
        // 这个方法在通用情况下不能直接工作，需要配合特定的UserDetails实现
        return false;
    }

    /**
     * 验证密码是否匹配
     * @param uId 用户ID
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(Integer uId, CharSequence rawPassword, String encodedPassword) {
        String plainPassword = uId + rawPassword.toString();
        return AESUtil.validatePassword(plainPassword, encodedPassword);
    }
}
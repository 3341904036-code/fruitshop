package com.fruitshop.config;

import com.fruitshop.util.AESUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * AES密码编码器
 * 用于处理与数据库中AES加密密码的匹配
 */
@Component
public class AESPasswordEncoder implements PasswordEncoder {

    private static final Logger log = LoggerFactory.getLogger(AESPasswordEncoder.class);

    @Override
    public String encode(CharSequence rawPassword) {
        // 如需使用encode方法，可补充逻辑，否则保留异常但添加日志
        log.error("禁止直接调用AESPasswordEncoder.encode方法 - 加密应在业务层完成");
        throw new UnsupportedOperationException("AES加密应该在业务层完成，请勿直接调用此方法");
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 默认matches方法不支持，添加日志并抛异常（而非返回false，更易排查）
        log.error("调用了默认的matches方法 - 此方法不支持，请使用带uId的重载方法");
        throw new UnsupportedOperationException("请使用matches(Integer uId, CharSequence rawPassword, String encodedPassword)方法");
    }

    /**
     * 验证密码是否匹配
     * @param uId 用户ID
     * @param rawPassword 原始密码
     * @param encodedPassword 数据库中加密后的密码
     * @return 是否匹配
     */
    public boolean matches(Integer uId, CharSequence rawPassword, String encodedPassword) {
        if (uId == null || rawPassword == null || encodedPassword == null) {
            log.error("密码验证失败 - 参数为空: uId={}, rawPassword={}, encodedPassword={}",
                    uId, rawPassword, encodedPassword);
            return false;
        }
        String plainText = uId + rawPassword.toString();
        log.debug("AES密码验证 - 拼接明文: {}, 数据库加密密码: {}", plainText.replaceAll(".", "*"), encodedPassword);
        return AESUtil.validatePassword(plainText, encodedPassword);
    }
}
package com.fruitshop.util;

import java.math.BigDecimal;

/**
 * VIP折扣计算工具类
 * VIP0: 0折扣 (1. 0)
 * VIP1: 0.1折扣 (0.9)
 * VIP2: 0.2折扣 (0.8)
 * VIP3: 0.3折扣 (0.7)
 */
public class VipDiscountUtil {

    /**
     * 根据VIP等级获取折扣系数
     */
    public static Double getDiscountRate(Integer vipLevel) {
        if (vipLevel == null || vipLevel == 0) {
            return 1.0; // 无折扣
        }
        switch (vipLevel) {
            case 1:
                return 0.9; // 10%折扣
            case 2:
                return 0.8; // 20%折扣
            case 3:
                return 0.7; // 30%折扣
            default:
                return 1.0;
        }
    }

    /**
     * 根据消费总额判断VIP等级
     */
    public static Integer getVipLevel(Double totalSpent) {
        if (totalSpent == null || totalSpent < 100) {
            return 0; // 普通用户
        } else if (totalSpent < 200) {
            return 1; // VIP1
        } else if (totalSpent < 500) {
            return 2; // VIP2
        } else {
            return 3; // VIP3
        }
    }

    /**
     * 计算应付金额
     */
    public static BigDecimal calculatePayAmount(BigDecimal originalPrice, Integer vipLevel) {
        Double discountRate = getDiscountRate(vipLevel);
        return originalPrice.multiply(BigDecimal.valueOf(discountRate));
    }

    /**
     * 根据消费总额更新VIP等级
     */
    public static Integer updateVipLevel(Double totalSpent) {
        return getVipLevel(totalSpent);
    }
}
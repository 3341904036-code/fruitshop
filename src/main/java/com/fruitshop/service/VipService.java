package com.fruitshop.service;

import com.fruitshop.dto.VipDiscountDTO;
import java.util.List;

/**
 * VIP服务接口
 */
public interface VipService {

    /**
     * 更新顾客VIP等级和折扣
     */
    void updateVipLevel();

    /**
     * 获取顾客的VIP信息
     */
    VipDiscountDTO getVipInfo(Integer cId);

    /**
     * 获取所有VIP顾客
     */
    List<VipDiscountDTO> getAllVipCustomers();

    /**
     * 手动设置VIP等级
     */
    boolean setVipLevel(Integer cId, Integer vipLevel);

    int getVipCustomerCount();
}
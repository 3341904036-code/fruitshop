package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com. fruitshop.dao.mapper.CustomerMapper;
import com.fruitshop.dao.mapper.OrderMapper;
import com.fruitshop.dto.VipDiscountDTO;
import com.fruitshop. entity.Customer;
import com. fruitshop.service.VipService;
import com.fruitshop.util. VipDiscountUtil;
import lombok.extern.slf4j. Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * VIP服务实现类
 */
@Slf4j
@Service
@Transactional
public class VipServiceImpl implements VipService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void updateVipLevel() {
        log.info("更新所有顾客VIP等级");

        List<Customer> customers = customerMapper.selectList(null);

        for (Customer customer : customers) {
            // 查询顾客的消费总额
            Double totalSpent = orderMapper.selectTotalSpentByCustomer(customer.getCId());
            if (totalSpent == null) {
                totalSpent = 0.0;
            }

            // 根据消费总额判断VIP等级
            Integer newVipLevel = VipDiscountUtil.getVipLevel(totalSpent);
            Double discount = VipDiscountUtil.getDiscountRate(newVipLevel);

            // 更新顾客VIP等级和折扣
            if (! newVipLevel.equals(customer. getCVip())) {
                customer.setCVip(newVipLevel);
                customer.setDiscount(discount);
                customerMapper.updateById(customer);
                log.info("VIP等级更新:  cId={}, vipLevel={}, discount={}",
                        customer.getCId(), newVipLevel, discount);
            }
        }

        log.info("VIP等级更新完成");
    }

    @Override
    public VipDiscountDTO getVipInfo(Integer cId) {
        Customer customer = customerMapper.selectById(cId);
        if (customer == null) {
            return null;
        }

        Double totalSpent = orderMapper.selectTotalSpentByCustomer(cId);
        if (totalSpent == null) {
            totalSpent = 0.0;
        }

        Double discountRate = VipDiscountUtil.getDiscountRate(customer. getCVip());

        return VipDiscountDTO.builder()
                .cId(customer.getCId())
                .cName(customer.getCName())
                .vipLevel(customer. getCVip())
                .totalSpent(java.math.BigDecimal.valueOf(totalSpent))
                .discountRate(discountRate)
                .build();
    }

    @Override
    public List<VipDiscountDTO> getAllVipCustomers() {
        List<Customer> customers = customerMapper.selectAllVipCustomers();
        List<VipDiscountDTO> vipList = new ArrayList<>();

        for (Customer customer : customers) {
            Double totalSpent = orderMapper.selectTotalSpentByCustomer(customer.getCId());
            if (totalSpent == null) {
                totalSpent = 0.0;
            }

            Double discountRate = VipDiscountUtil.getDiscountRate(customer.getCVip());

            VipDiscountDTO dto = VipDiscountDTO.builder()
                    .cId(customer.getCId())
                    .cName(customer.getCName())
                    .vipLevel(customer.getCVip())
                    .totalSpent(java.math.BigDecimal.valueOf(totalSpent))
                    .discountRate(discountRate)
                    .build();

            vipList.add(dto);
        }

        return vipList;
    }

    @Override
    public boolean setVipLevel(Integer cId, Integer vipLevel) {
        log.info("手动设置VIP等级:  cId={}, vipLevel={}", cId, vipLevel);

        Customer customer = customerMapper.selectById(cId);
        if (customer == null) {
            log.error("顾客不存在:  cId={}", cId);
            return false;
        }

        Double discount = VipDiscountUtil.getDiscountRate(vipLevel);
        customer.setCVip(vipLevel);
        customer.setDiscount(discount);

        int result = customerMapper.updateById(customer);
        log.info("VIP等级设置成功: cId={}, vipLevel={}, discount={}", cId, vipLevel, discount);
        return result > 0;
    }

    @Override
    public int getVipCustomerCount() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("cVip", 0);
        Long vipCount = customerMapper.selectCount(queryWrapper);
        return vipCount == null ? 0 : vipCount.intValue();
    }
}
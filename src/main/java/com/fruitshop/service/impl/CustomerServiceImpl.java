package com. fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fruitshop.dao.mapper.CustomerMapper;
import com.fruitshop.dao.mapper.OrderMapper;
import com.fruitshop.entity.Customer;
import com.fruitshop. exception.BusinessException;
import com. fruitshop.service.CustomerService;
import com.fruitshop.util.VipDiscountUtil;
import com.fruitshop.vo. CustomerVO;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.stereotype.Service;
import org.springframework.transaction.annotation. Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream. Collectors;

/**
 * 顾客服务实现类
 */
@Slf4j
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean addCustomer(Customer customer) {
        log.info("新增顾客: cName={}", customer.getCName());

        if (customer.getCVip() == null) {
            customer.setCVip(0);
        }

        // 设置初始折扣
        Double discount = VipDiscountUtil.getDiscountRate(customer.getCVip());
        customer.setDiscount(discount);

        int result = customerMapper.insert(customer);
        log.info("顾客新增成功: cId={}", customer.getCId());
        return result > 0;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        log.info("修改顾客: cId={}", customer.getCId());

        Customer existingCustomer = customerMapper. selectById(customer.getCId());
        if (existingCustomer == null) {
            throw new BusinessException("顾客不存在");
        }

        int result = customerMapper.updateById(customer);
        log.info("顾客修改成功:  cId={}", customer.getCId());
        return result > 0;
    }

    @Override
    public boolean deleteCustomer(Integer cId) {
        log.info("删除顾客: cId={}", cId);

        int result = customerMapper.deleteById(cId);
        log.info("顾客删除成功: cId={}", cId);
        return result > 0;
    }

    @Override
    public CustomerVO getCustomerById(Integer cId) {
        Customer customer = customerMapper.selectById(cId);
        if (customer == null) {
            return null;
        }
        return convertToVO(customer);
    }

    @Override
    public List<CustomerVO> getAllCustomers() {
        List<Customer> customers = customerMapper.selectList(null);
        return customers.stream().map(this::convertToVO).collect(Collectors. toList());
    }

    @Override
    public List<CustomerVO> getVipCustomers() {
        List<Customer> customers = customerMapper.selectAllVipCustomers();
        return customers. stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<CustomerVO> getCustomersByCity(String city) {
        List<Customer> customers = customerMapper.selectByCity(city);
        return customers.stream().map(this::convertToVO).collect(Collectors. toList());
    }

    @Override
    public Double getTotalSpentByCustomer(Integer cId) {
        Double total = orderMapper.selectTotalSpentByCustomer(cId);
        return total != null ? total : 0.0;
    }

    @Override
    public int count() {
        return customerMapper.selectCount(null) == null ? 0 : customerMapper.selectCount(null).intValue();
    }

    /**
     * 将Customer转换为CustomerVO
     */
    private CustomerVO convertToVO(Customer customer) {
        String vipLevel;
        switch (customer.getCVip()) {
            case 1:
                vipLevel = "VIP1";
                break;
            case 2:
                vipLevel = "VIP2";
                break;
            case 3:
                vipLevel = "VIP3";
                break;
            default:
                vipLevel = "普通";
        }

        return CustomerVO.builder()
                .cId(customer.getCId())
                .cName(customer.getCName())
                .cAddress(customer.getCAddress())
                .cCity(customer.getCCity())
                .cZip(customer.getCZip())
                .cContact(customer.getCContact())
                .cEmail(customer.getCEmail())
                .cVip(customer.getCVip())
                .vipLevel(vipLevel)
                .discount(customer.getDiscount())
                .build();
    }
}
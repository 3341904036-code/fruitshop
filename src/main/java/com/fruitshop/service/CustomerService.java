package com.fruitshop.service;

import com.fruitshop.entity.Customer;
import com.fruitshop.vo.CustomerVO;
import java.util.List;

/**
 * 顾客服务接口
 */
public interface CustomerService {

    /**
     * 新增顾客
     */
    boolean addCustomer(Customer customer);

    /**
     * 修改顾客信息
     */
    boolean updateCustomer(Customer customer);

    /**
     * 删除顾客
     */
    boolean deleteCustomer(Integer cId);

    /**
     * 查询单个顾客
     */
    CustomerVO getCustomerById(Integer cId);

    /**
     * 查��所有顾客
     */
    List<CustomerVO> getAllCustomers();

    /**
     * 查询VIP顾客
     */
    List<CustomerVO> getVipCustomers();

    /**
     * 根据城市查询顾客
     */
    List<CustomerVO> getCustomersByCity(String city);

    /**
     * 查询顾客的消费总额
     */
    Double getTotalSpentByCustomer(Integer cId);
}
package com.fruitshop.controller;

import com.fruitshop.entity.Customer;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.service.CustomerService;
import com.fruitshop. util.ResponseUtil;
import com.fruitshop.vo.CustomerVO;
import lombok.extern.slf4j. Slf4j;
import org. springframework.beans.factory.annotation. Autowired;
import org. springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 顾客控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 新增顾客
     */
    @PostMapping("/add")
    public ResponseUtil<? > addCustomer(@RequestBody Customer customer) {
        try {
            log.info("新增顾客请求: cName={}", customer.getCName());

            boolean result = customerService.addCustomer(customer);

            if (result) {
                log.info("顾客新增成功:  cName={}", customer.getCName());
                return ResponseUtil.success("新增成功", customer);
            } else {
                return ResponseUtil.error("新增失败");
            }
        } catch (BusinessException e) {
            log.error("新增顾客失败: {}", e.getMessage());
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("新增顾客异常", e);
            return ResponseUtil.error("新增顾客异常");
        }
    }

    /**
     * 修改顾客
     */
    @PutMapping("/update")
    public ResponseUtil<?> updateCustomer(@RequestBody Customer customer) {
        try {
            log.info("修改顾客请求: cId={}", customer.getCId());

            boolean result = customerService.updateCustomer(customer);

            if (result) {
                log.info("顾客修改成功: cId={}", customer.getCId());
                return ResponseUtil.success("修改成功");
            } else {
                return ResponseUtil.error("修改失败");
            }
        } catch (BusinessException e) {
            log.error("修改顾客失败: {}", e.getMessage());
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("修改顾客异常", e);
            return ResponseUtil.error("修改顾客异常");
        }
    }

    /**
     * 删除顾客
     */
    @DeleteMapping("/delete/{cId}")
    public ResponseUtil<?> deleteCustomer(@PathVariable Integer cId) {
        try {
            log.info("删除顾客请求: cId={}", cId);

            boolean result = customerService. deleteCustomer(cId);

            if (result) {
                log.info("顾客删除成功: cId={}", cId);
                return ResponseUtil.success("删除成功");
            } else {
                return ResponseUtil.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除顾客异常", e);
            return ResponseUtil.error("删除顾客异常");
        }
    }

    /**
     * 查询单个顾客
     */
    @GetMapping("/{cId}")
    public ResponseUtil<?> getCustomer(@PathVariable Integer cId) {
        try {
            log.info("查询顾客请求:  cId={}", cId);

            CustomerVO customer = customerService.getCustomerById(cId);

            if (customer != null) {
                return ResponseUtil.success("查询成功", customer);
            } else {
                return ResponseUtil.error(404, "顾客不存在");
            }
        } catch (Exception e) {
            log.error("查询顾客异常", e);
            return ResponseUtil.error("查询顾客异常");
        }
    }

    /**
     * 查询所有顾客
     */
    @GetMapping("/list/all")
    public ResponseUtil<? > getAllCustomers() {
        try {
            log.info("查询所有顾客");

            List<CustomerVO> customers = customerService.getAllCustomers();

            return ResponseUtil.success("查询成功", customers);
        } catch (Exception e) {
            log.error("查询顾客列表异常", e);
            return ResponseUtil.error("查询顾客列表异常");
        }
    }

    /**
     * 查询VIP顾客
     */
    @GetMapping("/list/vip")
    public ResponseUtil<?> getVipCustomers() {
        try {
            log.info("查询VIP顾客");

            List<CustomerVO> customers = customerService.getVipCustomers();

            return ResponseUtil. success("查询成功", customers);
        } catch (Exception e) {
            log.error("查询VIP顾客异常", e);
            return ResponseUtil.error("查询VIP顾客异常");
        }
    }

    /**
     * 根据城市查询顾客
     */
    @GetMapping("/list/city/{city}")
    public ResponseUtil<?> getCustomersByCity(@PathVariable String city) {
        try {
            log.info("根据城市查询顾客: city={}", city);

            List<CustomerVO> customers = customerService. getCustomersByCity(city);

            return ResponseUtil. success("查询成功", customers);
        } catch (Exception e) {
            log.error("根据城市查询顾客异常", e);
            return ResponseUtil.error("根据城市查询顾客异常");
        }
    }

    /**
     * 查询顾客消费总额
     */
    @GetMapping("/totalSpent/{cId}")
    public ResponseUtil<?> getTotalSpent(@PathVariable Integer cId) {
        try {
            log.info("查询顾客消费总额: cId={}", cId);

            Double total = customerService.getTotalSpentByCustomer(cId);

            return ResponseUtil.success("查询成功", total);
        } catch (Exception e) {
            log.error("查询消费总额异常", e);
            return ResponseUtil.error("查询消费总额异常");
        }
    }
}
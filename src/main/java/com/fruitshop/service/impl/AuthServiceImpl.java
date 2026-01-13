package com.fruitshop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com. fruitshop.dao.mapper.CustomerMapper;
import com.fruitshop.dao.mapper. SupplierMapper;
import com.fruitshop.dao.mapper.UserMapper;
import com.fruitshop.dto.LoginDTO;
import com.fruitshop.entity.Customer;
import com.fruitshop.entity. Supplier;
import com.fruitshop.entity.User;
import com.fruitshop. exception.BusinessException;
import com.fruitshop.service.AuthService;
import com.fruitshop.util.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework. beans.factory.annotation.Autowired;
import org.springframework. stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现类
 * 包含用户登录、注册、密码修改等功能
 */
@Slf4j
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SupplierMapper supplierMapper;

    /**
     * 用户登录
     */
    @Override
    public User login(LoginDTO loginDTO) {
        log.info("用户登录:   uId={}", loginDTO.getUId());

        try {
            Integer uId = Integer.parseInt(loginDTO.getUId());

            User user = userMapper.selectByUId(uId);

            if (user == null) {
                throw new BusinessException("用户不存在");
            }

            String plainPassword = uId + loginDTO.getPassword();
            String encryptedPassword = AESUtil.encrypt(plainPassword);

            if (!user.getPwd().equalsIgnoreCase(encryptedPassword)) {
                throw new BusinessException("密码错误");
            }

            log.info("用户登录成功:  uId={}, roleName={}", uId, user. getRoleName());
            return user;

        } catch (NumberFormatException e) {
            throw new BusinessException("用户ID格式错误");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("登录异常", e);
            throw new BusinessException("登录失败:  " + e.getMessage());
        }
    }

    /**
     * 用户注册 - 同时创建客户或供应商记录
     */
    @Override
    public boolean register(Integer uId, String password, String remark, Integer roleId) {
        log.info("用户注册:   uId={}, roleId={}, remark={}", uId, roleId, remark);

        try {
            // 1. 检查用户是否已存在
            User existingUser = userMapper.selectByUId(uId);
            if (existingUser != null) {
                throw new BusinessException("用户已存在");
            }

            // 2. 生成加密密码
            String plainPassword = uId + password;
            String encryptedPassword = AESUtil.encrypt(plainPassword);

            // 3. 创建用户记录
            User newUser = User.builder()
                    .uId(uId)
                    .pwd(encryptedPassword)
                    .remark(remark)
                    .roleId(roleId)
                    .roleName(getRoleNameByRoleId(roleId))
                    .build();

            int userResult = userMapper.insert(newUser);
            if (userResult <= 0) {
                throw new BusinessException("用户创建失败");
            }

            log.info("用户记录创建成功:  uId={}", uId);

            // 4. 根据角色创建对应的客户或供应商记录
            if (roleId == 2) {
                // 顾客角色 - 创建Customer记录
                createCustomerWithDetails(uId, remark);
            } else if (roleId == 3) {
                // 供应商角色 - 创建Supplier记录
                createSupplierWithDetails(uId, remark);
            }

            log.info("用户注册成功:  uId={}, roleId={}", uId, roleId);
            return true;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("注册异常", e);
            throw new BusinessException("注册失败:   " + e.getMessage());
        }
    }

    /**
     * 创建客户记录（包含详细信息）
     */
    private void createCustomerWithDetails(Integer uId, String remark) {
        try {
            Customer existingCustomer = customerMapper.selectOne(
                    new QueryWrapper<Customer>().eq("c_id", uId)
            );

            if (existingCustomer != null) {
                log.warn("客户记录已存在: cId={}", uId);
                return;
            }

            Customer customer = Customer.builder()
                    .cId(uId)
                    .cName(remark)
                    .cVip(0)
                    .build();

            int result = customerMapper.insert(customer);
            if (result > 0) {
                log. info("客户记录创建成功: cId={}, cName={}", uId, remark);
            } else {
                log.warn("客户记录创建失败:   cId={}", uId);
            }

        } catch (Exception e) {
            log.error("创建客户记录异常:   uId={}, error={}", uId, e.getMessage());
        }
    }

    /**
     * 创建供应商记录（包含详细信息）
     */
    private void createSupplierWithDetails(Integer uId, String remark) {
        try {
            Supplier existingSupplier = supplierMapper.  selectOne(
                    new QueryWrapper<Supplier>().eq("s_id", uId)
            );

            if (existingSupplier != null) {
                log.warn("供应商记录已存在: sId={}", uId);
                return;
            }

            Supplier supplier = Supplier.builder()
                    .  sId(uId)
                    .sName(remark)
                    .sCall("")
                    .build();

            int result = supplierMapper.insert(supplier);
            if (result > 0) {
                log.info("供应商记录创建成功: sId={}, sName={}", uId, remark);
            } else {
                log.warn("供应商记录创建失败:  sId={}", uId);
            }

        } catch (Exception e) {
            log.error("创建供应商记录异常:  uId={}, error={}", uId, e.  getMessage());
        }
    }

    /**
     * 修改密码
     */
    @Override
    public boolean changePassword(Integer uId, String oldPassword, String newPassword) {
        log.info("修改密码: uId={}", uId);

        try {
            User user = userMapper.selectByUId(uId);
            if (user == null) {
                throw new BusinessException("用户不存在");
            }

            String plainOldPassword = uId + oldPassword;
            String encryptedOldPassword = AESUtil.encrypt(plainOldPassword);

            if (!user. getPwd().equalsIgnoreCase(encryptedOldPassword)) {
                throw new BusinessException("旧密码错误");
            }

            String plainNewPassword = uId + newPassword;
            String encryptedNewPassword = AESUtil.encrypt(plainNewPassword);

            user.setPwd(encryptedNewPassword);
            int result = userMapper.updateById(user);

            log.info("密码修改成功: uId={}", uId);
            return result > 0;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("修改密码异常", e);
            throw new BusinessException("修改密码失败: " + e.getMessage());
        }
    }

    /**
     * 用户注销
     */
    @Override
    public boolean logout(Integer uId) {
        log.info("用户注销: uId={}", uId);
        return true;
    }

    /**
     * 验证密码
     */
    @Override
    public boolean validatePassword(String plainPassword, String encryptedPassword) {
        try {
            String encrypted = AESUtil.encrypt(plainPassword);
            return encrypted.equalsIgnoreCase(encryptedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 根据ID查询用户
     */
    @Override
    public User getUserById(Integer uId) {
        return userMapper.selectByUId(uId);
    }

    /**
     * 根据roleId获取角色名称
     */
    private String getRoleNameByRoleId(Integer roleId) {
        if (roleId == null) {
            return "Customer";
        }
        switch (roleId) {
            case 1:
                return "Admin";
            case 2:
                return "Cusm";  // Customer
            case 3:
                return "Supp";  // Supplier
            case 4:
                return "Salor";  // Salesperson
            default:
                return "Customer";
        }
    }
}
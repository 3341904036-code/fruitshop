package com.fruitshop.controller;

import com.fruitshop.dto.LoginDTO;
import com.fruitshop.entity.User;
import com.fruitshop.exception.BusinessException;
import com.fruitshop.service.AuthService;
import com. fruitshop.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework. beans.factory.annotation.Autowired;
import org.springframework. web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录 - REST API
     */
    @PostMapping("/login")
    public ResponseUtil<? > login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        try {
            log.info("========== 登录请求开始 ==========");
            log.info("请求用户ID: {}, 密码长度: {}", loginDTO.getUId(),
                    loginDTO.getPassword() != null ?  loginDTO.getPassword().length() : 0);

            User user = authService.login(loginDTO);

            // 将用户信息存入Session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUId());
            session.setAttribute("roleId", user.getRoleId());
            session.setAttribute("roleName", user.getRoleName());

            log.info("登录成功:  uId={}, roleName={}", user.getUId(), user.getRoleName());
            log.info("========== 登录请求结束 ==========");

            return ResponseUtil.success("登录成功", user);
        } catch (BusinessException e) {
            log.error("登录失败: {}", e.getMessage());
            log.info("========== 登录请求结束(失败) ==========");
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("登录异常", e);
            log.info("========== 登录请求结束(异常) ==========");
            return ResponseUtil.error("登录异常:  " + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseUtil<? > register(
            @RequestParam Integer uId,
            @RequestParam String password,
            @RequestParam Integer roleId,
            @RequestParam String remark,
            @RequestParam(required = false) String cEmail,
            @RequestParam(required = false) String cCity,
            @RequestParam(required = false) String cZip,
            @RequestParam(required = false) String cContact,
            @RequestParam(required = false) String sCity,
            @RequestParam(required = false) String sZip,
            @RequestParam(required = false) String sCall) {
        try {
            log.info("========== 注册请求开始 ==========");
            log.info("注册用户:  uId={}, roleId={}, remark={}", uId, roleId, remark);

            boolean result = authService.register(uId, password, remark, roleId);

            if (result) {
                log.info("用户注册成功:  uId={}", uId);
                log.info("========== 注册请求结束 ==========");
                return ResponseUtil.success("注册成功");
            } else {
                log.error("用户注册失败: uId={}", uId);
                log.info("========== 注册请求结束(失败) ==========");
                return ResponseUtil.error("注册失败");
            }
        } catch (BusinessException e) {
            log.error("注册失败: {}", e.getMessage());
            log.info("========== 注册请求结束(失败) ==========");
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("注册异常", e);
            log.info("========== 注册请求结束(异常) ==========");
            return ResponseUtil.error("注册异常: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public ResponseUtil<?> changePassword(
            @RequestParam Integer uId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        try {
            log.info("修改密码:  uId={}", uId);

            boolean result = authService.changePassword(uId, oldPassword, newPassword);

            if (result) {
                log.info("密码修改成功: uId={}", uId);
                return ResponseUtil.success("密码修改成功");
            } else {
                return ResponseUtil.error("密码修改失败");
            }
        } catch (BusinessException e) {
            log.error("密码修改失败: {}", e.getMessage());
            return ResponseUtil.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("密码修改异常", e);
            return ResponseUtil.error("密码修改异常: " + e.getMessage());
        }
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public ResponseUtil<?> logout(HttpSession session) {
        try {
            Integer uId = (Integer) session.getAttribute("userId");
            log.info("用户注销: uId={}", uId);

            authService.logout(uId);

            // 清除Session
            session.invalidate();

            log.info("用户注销成功: uId={}", uId);
            return ResponseUtil.success("注销成功");
        } catch (Exception e) {
            log.error("注销异常", e);
            return ResponseUtil.error("注销异常: " + e.getMessage());
        }
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/currentUser")
    public ResponseUtil<? > getCurrentUser(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null) {
                return ResponseUtil.error(401, "用户未登录");
            }

            return ResponseUtil.success("获取成功", user);
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return ResponseUtil.error("获取用户信息异常: " + e.getMessage());
        }
    }
}
package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户实体类（优化为显示「角色名+uId」）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    // 角色名映射（如果数据库中roleName是英文/缩写，转成中文显示）
    private static final Map<String, String> ROLE_MAP = new HashMap<>();
    static {
        ROLE_MAP.put("Cusm", "顾客");       // 匹配你日志中的roleName=Cusm
        ROLE_MAP.put("Supplier", "供货商"); // 供货商角色
        ROLE_MAP.put("Admin", "管理员");    // 管理员角色
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer uId; // 用户编号（核心显示字段）
    private String pwd;  // 密码
    private String remark;
    private Integer roleId;
    private String roleName; // 角色名（如Cusm/Supplier/Admin）

    // 页面显示专用：角色名（中文）+ uId
    public String getShowName() {
        // 1. 处理角色名（转中文，空则显示“未知角色”）
        String chineseRole = ROLE_MAP.getOrDefault(roleName, "未知角色");
        // 2. 处理uId（空则显示“未知编号”）
        String uidStr = uId != null ? uId.toString() : "未知编号";
        // 3. 拼接：比如“顾客10000”、“供货商100”
        return chineseRole + uidStr;
    }

    // ========== 以下为UserDetails接口实现（无需修改） ==========
    @Override
    public String getUsername() {
        // 登录用的用户名（返回uId字符串）
        return uId != null ? uId.toString() : "";
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = "ROLE_" + (roleName != null ? roleName : "CUSTOMER");
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
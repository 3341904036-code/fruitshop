package com.fruitshop.entity;

import com. baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus. annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 角色实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("roles")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType. AUTO)
    private Integer roleId;

    private String roleName;

    private String description;
}
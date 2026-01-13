package com.fruitshop.entity;

import com. baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus. annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok. Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType. AUTO)
    private Integer id;

    private Integer uId;

    private String pwd;

    private String remark;

    private Integer roleId;

    private String roleName;
}
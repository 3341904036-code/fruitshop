package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok.NoArgsConstructor;

import java. io.Serializable;

/**
 * 供应商实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("suppliers")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 供应商ID
     */
    @TableId
    private Integer sId;

    /**
     * 供应商名称
     */
    private String sName;

    /**
     * 供应商所在城市
     */
    private String sCity;

    /**
     * 邮编
     */
    private String sZip;

    /**
     * 联系电话
     */
    private String sCall;
}
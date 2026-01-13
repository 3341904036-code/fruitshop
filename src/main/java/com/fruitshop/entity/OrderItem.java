package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.  annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.  Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.  Serializable;
import java.math.BigDecimal;

/**
 * 订单项目实体类
 * 注意：该表使用复合主键 (o_num, o_item)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orderitems")
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号（复合主键的第一部分）
     */
    @TableId
    private Integer oNum;

    /**
     * 订单项序号（复合主键的第二部分）
     */
    private Integer oItem;

    /**
     * 水果ID
     */
    private String fId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 项目价格（单位价格）
     */
    private BigDecimal itemPrice;
}
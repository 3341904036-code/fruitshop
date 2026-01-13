package com.fruitshop. entity;

import com.baomidou.mybatisplus. annotation.IdType;
import com.baomidou.mybatisplus. annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "o_num", type = IdType.AUTO)
    private Integer oNum;

    private LocalDateTime oDate;

    private Integer cId;

    private BigDecimal originalPrice;

    private BigDecimal discount;

    private BigDecimal pay;
}
package com.fruitshop.dto;

import lombok. AllArgsConstructor;
import lombok.Builder;
import lombok. Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * VIP折扣数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VipDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer cId;

    private String cName;

    private Integer vipLevel;

    private BigDecimal totalSpent;

    private Double discountRate;
}
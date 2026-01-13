package com.fruitshop.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math. BigDecimal;

/**
 * 订单项目视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer oNum;

    private Integer oItem;

    private String fId;

    private String fName;

    private Integer quantity;

    private BigDecimal itemPrice;

    private BigDecimal subtotal;
}
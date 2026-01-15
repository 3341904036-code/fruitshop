package com.fruitshop.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer oNum;
    private LocalDateTime oDate;
    private Integer cId;
    private BigDecimal originalPrice;
    private BigDecimal discount;
    private BigDecimal pay;

    // ========== 新增：订单商品项列表 ==========
    private List<OrderItemVO> orderItems;
}
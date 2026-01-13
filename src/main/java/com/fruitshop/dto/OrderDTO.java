package com. fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据传输对象 (DTO)
 * 用于前后端交互时的数据转换
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private Integer oNum;

    /**
     * 订单日期
     */
    private LocalDateTime oDate;

    /**
     * 顾客ID
     */
    private Integer cId;

    /**
     * 原始价格（订单项总金额）
     */
    private BigDecimal originalPrice;

    /**
     * 折扣系数（默认1.0表示无折扣）
     */
    private BigDecimal discount;

    /**
     * 应付款（原价 × 折扣）
     */
    private BigDecimal pay;

    /**
     * 订单项目列表
     */
    private List<OrderItemDTO> items;

    /**
     * Getter方法 - getCId()
     */
    public Integer getCId() {
        return cId;
    }

    /**
     * Getter方法 - getItems()
     */
    public List<OrderItemDTO> getItems() {
        return items;
    }

    /**
     * Getter方法 - getONum()
     */
    public Integer getONum() {
        return oNum;
    }

    /**
     * Getter方法 - getODate()
     */
    public LocalDateTime getODate() {
        return oDate;
    }

    /**
     * Getter方法 - getOriginalPrice()
     */
    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    /**
     * Getter方法 - getDiscount()
     */
    public BigDecimal getDiscount() {
        return discount;
    }

    /**
     * Getter方法 - getPay()
     */
    public BigDecimal getPay() {
        return pay;
    }
}
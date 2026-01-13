package com.fruitshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单项目数据传输对象 (DTO)
 * 表示订单中的单个商品信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单号
     */
    private Integer oNum;

    /**
     * 订单项序号
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

    /**
     * Getter方法 - getFId()
     */
    public String getFId() {
        return fId;
    }

    /**
     * Getter方法 - getQuantity()
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Getter方法 - getONum()
     */
    public Integer getONum() {
        return oNum;
    }

    /**
     * Getter方法 - getOItem()
     */
    public Integer getOItem() {
        return oItem;
    }

    /**
     * Getter方法 - getItemPrice()
     */
    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    /**
     * Setter方法 - setFId()
     */
    public void setFId(String fId) {
        this.fId = fId;
    }

    /**
     * Setter方法 - setQuantity()
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Setter方法 - setONum()
     */
    public void setONum(Integer oNum) {
        this.oNum = oNum;
    }

    /**
     * Setter方法 - setOItem()
     */
    public void setOItem(Integer oItem) {
        this.oItem = oItem;
    }

    /**
     * Setter方法 - setItemPrice()
     */
    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }
}
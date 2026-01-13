package com.fruitshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus. annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 水果实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("fruits")
public class Fruit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "f_id")
    private String fId;

    private Integer sId;

    private String fName;

    private BigDecimal fPrice;

    private Integer quantity;
}
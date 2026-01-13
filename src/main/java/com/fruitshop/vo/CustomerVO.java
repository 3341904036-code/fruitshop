package com.fruitshop.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 顾客视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer cId;

    private String cName;

    private String cAddress;

    private String cCity;

    private String cZip;

    private String cContact;

    private String cEmail;

    private Integer cVip;

    private String vipLevel;

    private Double discount;
}
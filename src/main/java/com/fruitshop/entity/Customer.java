package com.fruitshop.entity;

import com. baomidou.mybatisplus.annotation.IdType;
import com.baomidou. mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus. annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 顾客实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("customers")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "c_id", type = IdType. AUTO)
    private Integer cId;

    private String cName;

    private String cAddress;

    private String cCity;

    private String cZip;

    private String cContact;

    private String cEmail;

    private Integer cVip;

    private Double discount;
}
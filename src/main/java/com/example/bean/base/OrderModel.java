package com.example.bean.base;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单表
 */
@Data
public class OrderModel {

    private String order_id;
    private Integer order_state; //订单状态  0正常 1已支付 2交易完成
    private BigDecimal order_money; //订单金额

}

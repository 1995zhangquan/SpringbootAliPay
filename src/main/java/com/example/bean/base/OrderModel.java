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
    private Integer order_state; //订单状态  0初始创建 1充值 -1退款
    private BigDecimal order_money; //订单金额
    private String order_p_id; //父级订单id， 部分退款时记录父级订单
}

package com.example.bean.business;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 结算model：最后综合结算model
 */
@Data
public class BalanceAmountModel {

    private String balance_id; //订单id
    private String balance_title; //商品标题
    private BigDecimal balance_money; //订单金额
    private String balance_introduce; //订单介绍

}

package com.example.bean.business;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 请求参数集合
 */
@Data
public class AliPayParamsModel {

    private String out_trade_no; //订单号
    private BigDecimal total_amount; //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
    private String subject; //订单标题,对应界面的商品标题
    private final String product_code = "FAST_INSTANT_TRADE_PAY"; //销售产品码
    private String time_expire; //订单绝对超时时间。格式为yyyy-MM-dd HH:mm:ss。注：time_expire和timeout_express两者只需传入一个或者都不传，两者均传入时，优先使用time_expire。
    private String body; //商品描述

    private String out_request_no; //退款请求号，部分退款时必填
    private BigDecimal refund_amount; //需要退款的金额，该金额不能大于订单金额，必填
    private String refund_reason; //退款的原因说明

}

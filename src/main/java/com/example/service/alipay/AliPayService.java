package com.example.service.alipay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.example.bean.base.OrderModel;
import com.example.bean.business.BalanceAmountModel;
import com.example.config.alipay.AliPayConfig;
import com.example.dao.OrderDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
@Slf4j
@Service
public final class AliPayService {

    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private OrderDao orderDao;

    public String pay(BalanceAmountModel balanceModel) throws AlipayApiException {
        if (balanceModel.getBalance_money().compareTo(new BigDecimal(0)) <= 0 || balanceModel.getBalance_money().compareTo(new BigDecimal(100000000L)) > 0) {
            return "金额范围不正确！";
        }
        //初始化alipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.getGatewayUrl(),
                aliPayConfig.getApp_id(), aliPayConfig.getMerchant_private_key(), "Json",
                aliPayConfig.getCharset(), aliPayConfig.getAlipay_public_key(), aliPayConfig.getSign_type());


        AlipayTradePagePayRequest payRequest = new AlipayTradePagePayRequest();
        payRequest.setReturnUrl(aliPayConfig.getReturn_url());
        payRequest.setNotifyUrl(aliPayConfig.getNotify_url());
        //设置参数的集合
        Map<String, Object> bizContent = new HashMap<String, Object>();
        bizContent.put("out_trade_no", balanceModel.getBalance_id()); //商户订单号
        bizContent.put("total_amount", balanceModel.getBalance_money()); //订单总金额
        bizContent.put("subject", balanceModel.getBalance_title()); //商品标题
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY"); //销售产品码
        bizContent.put("body", balanceModel.getBalance_introduce()); //商品描述
        //请求参数都要放置在bizContent里
        payRequest.setBizContent(new JSONObject(bizContent).toString());
        //执行支付操作
        AlipayTradePagePayResponse payResponse = alipayClient.pageExecute(payRequest);
        String body = payResponse.getBody();
        return body;
    }

    public String query(String out_trade_no) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfig.gatewayUrl, aliPayConfig.app_id, aliPayConfig.merchant_private_key, "json", aliPayConfig.charset, aliPayConfig.alipay_public_key, aliPayConfig.sign_type);
        AlipayTradeQueryRequest queryRequest = new AlipayTradeQueryRequest();
        JSONObject outTradeNoObj = new JSONObject();
        //商户订单号，商户网站订单系统中唯一订单号
        outTradeNoObj.put("out_trade_no", out_trade_no);
        //支付宝交易号 和上面二选一
        //outTradeNoObj.put("trade_no")

        queryRequest.setBizContent(outTradeNoObj.toString());
        String body = alipayClient.execute(queryRequest).getBody();
        return body;
    }

    public boolean rsaCheckV1(Map<String,String[]> requestParams) throws UnsupportedEncodingException, AlipayApiException {
        Map<String,String> params = new HashMap<String,String>();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        boolean signVeritied = AlipaySignature.rsaCheckV1(params, aliPayConfig.alipay_public_key, aliPayConfig.charset, aliPayConfig.sign_type);//调用SDK验证签名
        return signVeritied;
    }

    /**
     *  1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
     *  2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
     *  3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
     *  4、验证app_id是否为该商户本身。
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean checkAliPayReturnParam(HttpServletRequest request) throws UnsupportedEncodingException {
        //商户订单号
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //订单金额
        BigDecimal total_amount = new BigDecimal(request.getParameter("total_amount"));
        //支付宝交易号
        String app_id = new String(request.getParameter("app_id").getBytes("ISO-8859-1"),"UTF-8");
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

        // 1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        OrderModel orderModel = orderDao.findByOrderId(out_trade_no);
        if (null == orderModel) {
            log.error("支付宝异步回调返回订单号:{},与系统订单不一致", out_trade_no);
            return false;
        }
        //2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        if (total_amount.compareTo(orderModel.getOrder_money()) != 0) {
            log.error("支付宝异步回调返回订单号：{}，金额:{},与系统订单金额：{}不一致", out_trade_no, total_amount, orderModel.getOrder_money());
            return false;
        }
        //3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        //4、验证app_id是否为该商户本身。
        if (!aliPayConfig.getApp_id().equals(app_id)) {
            log.error("支付宝异步回调app_id:{}与系统不一致");
            return false;
        }
        return true;
    }

}

package com.example.control;

import com.alipay.api.AlipayApiException;
import com.example.bean.base.OrderModel;
import com.example.bean.business.BalanceAmountModel;
import com.example.service.OrderService;
import com.example.service.alipay.AliPayService;
import com.example.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 沙箱账号
 * tjrned2206@sandbox.com
 * 111111
 */
@Slf4j
@Controller
@RequestMapping("alipay")
public class AlipayControl {

    @Resource
    private AliPayService aliPayService;
    @Resource
    private OrderService orderService;

    @RequestMapping("byPrice")
    public void byPrice(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException, IOException {
        Double totalMoneyDl = Double.valueOf(request.getParameter("totalMoney"));
        BigDecimal totalMoney = BigDecimal.valueOf(totalMoneyDl);
        //创建初始订单
        OrderModel model = new OrderModel();
        String orderId = IdUtil.createOrderId();
        model.setOrder_id(orderId);
        model.setOrder_state(0);
        model.setOrder_money(totalMoney);
        orderService.add(model);
        //调起支付接口
        BalanceAmountModel balanceAmountModel = new BalanceAmountModel();
        balanceAmountModel.setBalance_id(model.getOrder_id());
        balanceAmountModel.setBalance_introduce("购买小裤衩");
        balanceAmountModel.setBalance_title("小裤衩");
        balanceAmountModel.setBalance_money(totalMoney);
        String pay = aliPayService.pay(balanceAmountModel);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(pay);
        out.flush();
        out.close();
    }

    /**
     * 这一步是最后一步，交易完成
     * @param request
     * @param response
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, AlipayApiException {
        log.info("这是本地调用的returl uurl");
        boolean checkFlag = aliPayService.rsaCheckV1(request.getParameterMap());
        if (checkFlag) {
            String out_trade_no = request.getParameter("out_trade_no");
            String trade_no = request.getParameter("trade_no");
            String total_amount = request.getParameter("total_amount");
            request.setAttribute("out_trade_no", out_trade_no);
            request.setAttribute("trade_no", trade_no);
            request.setAttribute("total_amount", total_amount);
            //修改订单状态11
            OrderModel orderModel = orderService.findByOrderId(out_trade_no);
            orderModel.setOrder_state(2);
            orderService.update(orderModel);
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 调用该方法钱，同步请求已完成，页面就是 已成功支付n元
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws AlipayApiException
     */
    @RequestMapping("returnNotifyUrl")
    public String returnNotifyUrl(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {
        boolean checkFlag = aliPayService.rsaCheckV1(request.getParameterMap());
        if (checkFlag) {
            String out_trade_no = request.getParameter("out_trade_no");
            String trade_no = request.getParameter("trade_no");
            String trade_status = request.getParameter("trade_status");
            if(trade_status.equals("TRADE_FINISHED")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                return "fail";
                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            }else if (trade_status.equals("TRADE_SUCCESS")){
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
                boolean checkSameSys = aliPayService.checkAliPayReturnParam(request);
                if (checkSameSys) {
                    //修改订单状态
                    OrderModel orderModel = orderService.findByOrderId(out_trade_no);
                    orderModel.setOrder_state(1);
                    orderService.update(orderModel);
                    request.setAttribute("out_trade_no", out_trade_no);
                    request.setAttribute("trade_no", trade_no);
                    return "success";
                } else {
                    return "fail";
                }
            }
        }
        log.error("验签失败");
        return "fail";
    }


    @RequestMapping("showOrderList")
    public String showOrderList(HttpServletRequest request) {
        List<OrderModel> list = orderService.findList();
        request.setAttribute("orderList", list);
        return "orderList";
    }
}

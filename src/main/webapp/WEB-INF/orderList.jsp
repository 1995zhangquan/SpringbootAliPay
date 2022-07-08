<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022-07-05
  Time: 08:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>orderList</title>
</head>
<body>
<ul>
    <c:if test="${not empty orderList}">
        <c:forEach var="order" items="${orderList}">
            <li >订单id:<a href="javaScript:void(0)" oid="${order.order_id}"> ${order.order_id}</a> |
                订单状态:${order.order_state eq 0 ? '初始创建' : order.order_state eq 1 ? '充值' : order.order_state eq -1 ? '退款' : ''} | 订单金额：${order.order_money}
                <c:if test="${order.order_state eq 1}">
                    <span oid="${order.order_id}" id="refundMoney" style="color: crimson">退款</span>
                </c:if>
                <c:if test="${order.order_state eq 0}">
                    <span oid="${order.order_id}" id="payRightNows" style="color: green">立即支付</span>
                </c:if>
                <span oid="${order.order_id}" id="deleteOrder" style="color: blue">删除订单</span>
            </li>
        </c:forEach>
    </c:if>
    <c:if test="${empty orderList}">
        没有订单
        <a href="/"><button>返回</button></a>
    </c:if>
</ul>
</body>
<script type="text/javascript"src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script>
    $(function () {
        $("ul li").each(function () {
            let $this = $(this);
            var orderId = $this.find('a').click(function () {
                let orderId=$(this).attr('oid');
                $.post("/alipay/queryOrder", {orderId:orderId}, function (returnData) {
                    returnData = JSON.parse(returnData);
                    alert(returnData.alipay_trade_query_response.sub_msg);
                })
            });
            $this.find('#refundMoney').click(function () {
                window.location.href = "/alipay/toRefund?out_trade_no=" + $(this).attr('oid');
            });

            $this.find('#payRightNows').click(function () {
                window.location.href = "/alipay/byPrice?out_trade_no=" + $(this).attr('oid');
            });
            $this.find('#deleteOrder').click(function () {
                $.post("/alipay/deleteOrder",{out_trade_no:$(this).attr('oid')}, function (message) {
                    if ('success' == message) {
                        window.location.reload();
                    }
                })
            });
        });
    });
</script>
</html>

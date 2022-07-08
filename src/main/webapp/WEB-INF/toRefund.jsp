<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2022-07-05
  Time: 09:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>toRefund</title>
</head>
<body>
    <form id="refundForm" onsubmit="return false">
        <input name="out_trade_no" id="out_trade_no" value="${orderModel.order_id}">
        <table>
            <tr>
                <td>订单编号：</td>
                <td>${orderModel.order_id}</td>
            </tr>
            <tr>
                <td>订单金额：</td>
                <td>${orderModel.order_money}</td>
            </tr>
            <tr>
                <td>退款金额：</td>
                <td>
                    <input name="refund_amount" id="refund_amount" >
                </td>
            </tr>
            <tr>
                <td>退款原因：</td>
                <td>
                    <textarea name="refund_reason" id="refund_reason"></textarea>
                </td>
            </tr>
            <tr>
                <td colspan="2"><button id="refundBtn">确定</button></td>
            </tr>
        </table>
    </form>

</body>
<script type="text/javascript"src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('#refundBtn').click(function () {
            $.post("/alipay/refundMoney", $('#refundForm').serialize(), function (message) {
                console.log(message);
                alert(message);
            })
        });
    })
</script>
</html>

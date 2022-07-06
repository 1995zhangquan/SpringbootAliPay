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
    <c:forEach var="order" items="${orderList}">
        <li>订单id:${order.order_id} | 订单状态:${order.order_state eq 0 ? '正常' : order.order_state eq 1 ? '已支付' : order.order_state eq 2 ? '交易完成' : ''} | 订单金额：${order.order_money}</li>
    </c:forEach>
</ul>
</body>
</html>

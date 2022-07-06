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
    <title>hello</title>
</head>
<body>
    <table>
        <tr>
            <td>商品名称：</td>
            <td>小裤衩</td>
        </tr>
        <tr>
            <td>商品单价：</td>
            <td>￥1022
                <input type="hidden" value="1022" id="onePrice">
            </td>
        </tr>
        <tr>
            <td>批发量：</td>
            <td>
                <select id="byCount">
                    <option value="1">1件</option>
                    <option value="2">2件</option>
                    <option value="3">3件</option>
                    <option value="4">4件</option>
                    <option value="5">5件</option>
                    <option value="6">6件</option>
                    <option value="7">7件</option>
                    <option value="8">8件</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>结算价格：</td>
            <td>￥<span id="showTotal">1022</span></td>
        </tr>
        <tr>
            <td colspan="2">
                <button id="submitForm">确定</button>
                <button id="showOrderList">查看订单信息</button>
            </td>
        </tr>
    <br>
    </table>
</body>
<script type="text/javascript"src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">
    $(function () {
        $('#byCount').change(function () {
            var onePrice = $('#onePrice').val();
            var byCount = $('#byCount').val();
            $('#showTotal').text(byCount * onePrice);
        });

        $('#submitForm').click(function () {
            let totalMoney = $('#showTotal').text();
            $.ajax({
                type : "get",
                url : "/alipay/byPrice",
                data : { totalMoney:totalMoney},
                success : function(data) {
                    window.open().document.write(data);
                },
                error : function(xhr, ajaxOptions, throwError) {}
            });
        });

        $('#showOrderList').click(function () {
            window.location.href ='/alipay/showOrderList';
        });

    });

</script>
</html>

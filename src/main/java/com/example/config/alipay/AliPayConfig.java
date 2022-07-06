package com.example.config.alipay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *基础配置类，设置帐户有关信息及返回路径
 */
@Component
@Data
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {

    public String app_id; //用ID,您的APPID，收款账号既是您的APPID对应支付宝账号

    public String merchant_private_key; //商户应用私钥，您的PKCS8格式RSA2私钥

    public String alipay_public_key; //支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。

    public String notify_url; //服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问

    public String return_url; //页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问,支付成功后需要跳转的页面

    public String sign_type; //签名方式

    public String charset; //字符编码格式

    public String gatewayUrl; //支付宝网关

}


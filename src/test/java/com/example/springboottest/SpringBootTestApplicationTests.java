package com.example.springboottest;

import com.alipay.api.AlipayApiException;
import com.example.bean.base.OrderModel;
import com.example.bean.base.UserModel;
import com.example.bean.business.BalanceAmountModel;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.service.alipay.AliPayService;
import com.example.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@SpringBootTest
class SpringBootTestApplicationTests {


    @Resource
    private UserService service;
    @Resource
    private AliPayService aliPayService;
    @Resource
    private OrderService orderService;

    @Test
    void testAdd() {
        UserModel userModel = new UserModel();
        String timeId = IdUtil.createTimeId();
        userModel.setU_id(timeId);
        userModel.setU_name("李狗蛋");
        userModel.setU_phone("18255555555");
        userModel.setU_password("9527");
        service.add(userModel);
    }

    @Test
    void testDelete() {
        String userId = "1";
        service.delete(userId);
    }

    @Test
    void testUpdate() {
        List<UserModel> list = service.findList();
        UserModel userBean = list.get(0);
        userBean.setU_id(userBean.getU_id());
        userBean.setU_name("李狗蛋-改");
        userBean.setU_phone("18244444444");
        userBean.setU_password("9527-改");
        service.update(userBean);
    }

    @Test
    void testGetOne() {
        List<UserModel> list = service.findList();
        UserModel userBean = list.get(0);
        log.warn("userBean:{}", userBean.toString());
    }

    @Test
    void testGetList() {
        List<UserModel> list = service.findList();
        for (UserModel userBean : list) {
            log.warn("userBean:{}", userBean.toString());
        }
    }
}

package com.example.service;

import com.example.bean.base.OrderModel;
import com.example.bean.base.UserModel;
import com.example.dao.OrderDao;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderService {

    @Resource
    private OrderDao dao;

    public boolean add(OrderModel orderModel) {
        return dao.add(orderModel);
    }

    public List<OrderModel> findList() {
        return dao.findList();
    }
    public OrderModel findByOrderId(String orderId) {
        return dao.findByOrderId(orderId);
    }

    public boolean update(OrderModel model) {
        return dao.update(model);
    }
}

package com.example.dao;

import com.example.bean.base.OrderModel;
import com.example.bean.base.UserModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class OrderDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public boolean add(OrderModel orderModel) {
        String sql = "insert into base_order(order_id,order_state,order_money) values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int resRow = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{"order_id"});
                ps.setString(1,orderModel.getOrder_id());
                ps.setInt(2,orderModel.getOrder_state());
                ps.setBigDecimal(3,orderModel.getOrder_money());
                return ps;
            }
        },keyHolder);
        return resRow > 0;
    }

    public List<OrderModel> findList() {
        String sql = "select * from base_order";
        List<OrderModel> orderModel = jdbcTemplate.query(sql, new BeanPropertyRowMapper<OrderModel>(OrderModel.class));
        return orderModel;
    }

    public OrderModel findByOrderId(String orderId) {
        String sql = " select * from base_order where order_id = ? ";
        OrderModel orderModel = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<OrderModel>(OrderModel.class), orderId);
        return orderModel;
    }

    public boolean update(OrderModel orderModel) {
        String sql = "update base_order set order_state = ? ,order_money = ? where order_id= ?";
        Object[] args = {orderModel.getOrder_state(), orderModel.getOrder_money(), orderModel.getOrder_id()};
        int update = jdbcTemplate.update(sql, args);
        return update > 0;
    }
}

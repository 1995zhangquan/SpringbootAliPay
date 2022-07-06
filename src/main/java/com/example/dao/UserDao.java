package com.example.dao;

import com.example.bean.base.UserModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public boolean add(UserModel systemUser) {
        String sql = "insert into base_user(u_id,u_name,u_phone,u_password) values(?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int resRow = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,new String[]{"u_id"});
                ps.setString(1,systemUser.getU_id());
                ps.setString(2,systemUser.getU_name());
                ps.setString(3,systemUser.getU_phone());
                ps.setString(4,systemUser.getU_password());
                return ps;
            }
        },keyHolder);
        System.out.println("操作记录数："+resRow+" 主键："+keyHolder.getKey());
        return resRow > 0;
    }

    public boolean delete(String userId) {
        String sql = "delete from base_user where u_id=?";
        return jdbcTemplate.update(sql,userId) > 0;
    }

    public boolean update(UserModel systemUser) {
        String sql = "update base_user set u_name=?, u_phone=?, u_password =? where u_id=?";
        Object[] args = {systemUser.getU_name(),systemUser.getU_phone(),systemUser.getU_password(), systemUser.getU_id()};
        int update = jdbcTemplate.update(sql, args);
        return update > 0;
    }

    public UserModel findById(String userId) {
        String sql = "select * from base_user where u_id = ?";
        UserModel systemUser = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<UserModel>(UserModel.class),userId);
        return systemUser;
    }

    public List<UserModel> findList() {
        String sql = "select * from base_user";
        List<UserModel> bookList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserModel>(UserModel.class));
        return bookList;
    }
}

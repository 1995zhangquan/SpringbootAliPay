package com.example.service;

import com.example.bean.base.UserModel;
import com.example.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao dao;

    public boolean add(UserModel systemUser) {
        return dao.add(systemUser);
    }

    public boolean delete(String userId) {
        return dao.delete(userId);
    }
    public boolean update(UserModel systemUser) {
        return dao.update(systemUser);
    }
    public UserModel findById(String userId) {
        return dao.findById(userId);
    }

    public List<UserModel> findList() {
        return dao.findList();
    }

}

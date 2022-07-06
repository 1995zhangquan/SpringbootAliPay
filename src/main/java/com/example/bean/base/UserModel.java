package com.example.bean.base;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserModel implements Serializable {

    private String u_id;// 用户主键
    private String u_name; //昵称
    private String u_phone; //手机
    private String u_password; //密码

}

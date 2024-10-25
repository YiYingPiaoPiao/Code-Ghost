package com.alicia.projects.entity;

import com.alicia.projects.util.enumAccessLvl;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("User")
@Data
public class entityUser implements Serializable {

    @Id
    private String _id;         // 系统生成 ID

    private String usrId    ;   // 用户 ID，学生为学号，教师为教师的号码
    private String usrName  ;   // 用户名

    private String        usrPassword;  // 用户设置的密码
    private enumAccessLvl accessLvl  ;  // 用户的权限

    public entityUser () {}
}

package com.demo.swagger.swagger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MallUser implements Serializable {
    private Long userId;

    private String nickName;

    private String loginName;

    private String passwordMD5;

    private String introduceSign;

    private Byte isDeleted;

    private Byte lockedFlag;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private Date createTime;
}

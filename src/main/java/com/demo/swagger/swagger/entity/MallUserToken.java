package com.demo.swagger.swagger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MallUserToken {
    private Long userId;

    private String token;

    private Date updateTime;

    private Date expireTime;
}

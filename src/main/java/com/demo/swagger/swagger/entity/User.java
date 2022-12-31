package com.demo.swagger.swagger.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String name;
    private String password;
}

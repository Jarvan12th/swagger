package com.demo.swagger.swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Result<T> implements Serializable {

    @ApiModelProperty("Result Code")
    private int resultCode;

    @ApiModelProperty("Message")
    private String message;

    @ApiModelProperty("Data")
    private T data;

    public Result(int resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" + "resultCode=" + resultCode + ", message=" + message + ", data=" + data + "}";
    }
}

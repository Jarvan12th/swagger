package com.demo.swagger.swagger.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class NewBeeMallOrderDetailVO implements Serializable {

    @ApiModelProperty("Order No")
    private String orderNo;

    @ApiModelProperty("Total Price")
    private Integer totalPrice;

    @ApiModelProperty("Pay Status")
    private Byte payStatus;

    @ApiModelProperty("Pay Type")
    private Byte payType;

    @ApiModelProperty("Pay Type in String")
    private String payTypeString;

    @ApiModelProperty("Pay Time")
    private Date payTime;

    @ApiModelProperty("Order Status")
    private Byte orderStatus;

    @ApiModelProperty("Order Status in String")
    private String orderStatusString;

    @ApiModelProperty("Create Time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT")
    private Date createTime;

    @ApiModelProperty("Order Item List")
    private List<NewBeeMallOrderItemVO> newBeeMallOrderItemVOS;
}

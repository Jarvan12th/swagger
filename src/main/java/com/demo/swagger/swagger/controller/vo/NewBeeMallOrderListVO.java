package com.demo.swagger.swagger.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class NewBeeMallOrderListVO implements Serializable {

    @ApiModelProperty("Order Id")
    private Long orderId;

    @ApiModelProperty("Order No")
    private String orderNo;

    @ApiModelProperty("Order Total Price")
    private Integer totalPrice;

    @ApiModelProperty("Order Pay Type")
    private Byte payType;

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

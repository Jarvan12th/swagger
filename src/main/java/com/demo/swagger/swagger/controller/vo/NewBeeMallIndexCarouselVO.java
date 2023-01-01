package com.demo.swagger.swagger.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewBeeMallIndexCarouselVO implements Serializable {

    @ApiModelProperty("Carousel Image Url")
    private  String carouselUrl;

    @ApiModelProperty("Carousel Image Redirect Url")
    private String redirectUrl;
}

package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexConfigGoodsVO;

import java.util.List;

public interface NewBeeMallIndexConfigService {

    List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number);
}

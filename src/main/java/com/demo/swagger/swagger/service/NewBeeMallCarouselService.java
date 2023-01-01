package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexCarouselVO;

import java.util.List;

public interface NewBeeMallCarouselService {
    List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number);
}

package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexCarouselVO;
import com.demo.swagger.swagger.dao.CarouselMapper;
import com.demo.swagger.swagger.entity.Carousel;
import com.demo.swagger.swagger.service.NewBeeMallCarouselService;
import com.demo.swagger.swagger.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewBeeMallCarouselServiceImpl implements NewBeeMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public List<NewBeeMallIndexCarouselVO> getCarouselsForIndex(int number) {

        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            return BeanUtils.copyList(carousels, NewBeeMallIndexCarouselVO.class);
        }

        return new ArrayList<>();
    }
}

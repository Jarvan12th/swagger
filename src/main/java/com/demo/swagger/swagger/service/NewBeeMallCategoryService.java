package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexCategoryVO;

import java.util.List;

public interface NewBeeMallCategoryService {

    List<NewBeeMallIndexCategoryVO> getCategoriesForIndex();
}

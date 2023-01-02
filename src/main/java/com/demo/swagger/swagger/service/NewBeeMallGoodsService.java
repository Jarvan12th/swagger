package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;

public interface NewBeeMallGoodsService {

    PageResult searchNewBeeMallGoods(PageQueryUtils pageQueryUtils);

    NewBeeMallGoods getNewBeeMallGoodsById(Long goodsId);
}

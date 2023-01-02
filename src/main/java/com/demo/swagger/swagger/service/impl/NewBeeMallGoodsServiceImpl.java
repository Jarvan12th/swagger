package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.controller.vo.NewBeeMallSearchGoodsVO;
import com.demo.swagger.swagger.dao.NewBeeMallGoodsMapper;
import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.service.NewBeeMallGoodsService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class NewBeeMallGoodsServiceImpl implements NewBeeMallGoodsService {

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public PageResult searchNewBeeMallGoods(PageQueryUtils pageQueryUtils) {
        List<NewBeeMallGoods> goodsList = newBeeMallGoodsMapper.findNewBeeMallGoodsListBySearch(pageQueryUtils);
        int total = newBeeMallGoodsMapper.getTotalNewBeeMallGoodsBySearch(pageQueryUtils);
        List<NewBeeMallSearchGoodsVO> newBeeMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            newBeeMallSearchGoodsVOS = BeanUtils.copyList(goodsList, NewBeeMallSearchGoodsVO.class);
            for (NewBeeMallSearchGoodsVO newBeeMallSearchGoodsVO : newBeeMallSearchGoodsVOS) {
                String goodsName = newBeeMallSearchGoodsVO.getGoodsName();
                newBeeMallSearchGoodsVO.setGoodsName(goodsName.length() > 28 ? goodsName.substring(0, 28) + "..." : goodsName);

                String goodsIntro = newBeeMallSearchGoodsVO.getGoodsIntro();
                newBeeMallSearchGoodsVO.setGoodsIntro(goodsIntro.length() > 30 ? goodsIntro.substring(0, 30) + "..." : goodsIntro);
            }
        }

        return new PageResult(newBeeMallSearchGoodsVOS, total, pageQueryUtils.getLimit(), pageQueryUtils.getPage());
    }

    @Override
    public NewBeeMallGoods getNewBeeMallGoodsById(Long goodsId) {
        return newBeeMallGoodsMapper.selectByPrimaryKey(goodsId);
    }
}

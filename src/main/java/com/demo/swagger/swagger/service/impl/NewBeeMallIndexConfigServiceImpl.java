package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexConfigGoodsVO;
import com.demo.swagger.swagger.dao.IndexConfigMapper;
import com.demo.swagger.swagger.dao.NewBeeMallGoodsMapper;
import com.demo.swagger.swagger.entity.IndexConfig;
import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.service.NewBeeMallIndexConfigService;
import com.demo.swagger.swagger.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewBeeMallIndexConfigServiceImpl implements NewBeeMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public List<NewBeeMallIndexConfigGoodsVO> getConfigGoodsForIndex(int configType, int number) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigByTypeAndNumber(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            List<Long> goodIds = indexConfigs.stream().map(IndexConfig::getGoodId).collect(Collectors.toList());
            List<NewBeeMallGoods> newBeeMallGoods = newBeeMallGoodsMapper.selectGoodsByPrimaryKeys(goodIds);
            return BeanUtils.copyList(newBeeMallGoods, NewBeeMallIndexConfigGoodsVO.class).stream()
                    .map(good -> trimGoodName(good)).collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    private NewBeeMallIndexConfigGoodsVO trimGoodName(NewBeeMallIndexConfigGoodsVO good) {
        String goodName = good.getGoodsName();
        if (goodName.length() > 30) {
            goodName = goodName.substring(0, 30) + "...";
        }

        good.setGoodsName(goodName);
        return good;
    }
}

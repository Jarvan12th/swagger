package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallCategoryLevelEnum;
import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexCategoryVO;
import com.demo.swagger.swagger.controller.vo.SecondaryLevelCategoryVO;
import com.demo.swagger.swagger.controller.vo.ThirdLevelCategoryVO;
import com.demo.swagger.swagger.dao.GoodsCategoryMapper;
import com.demo.swagger.swagger.entity.GoodsCategory;
import com.demo.swagger.swagger.service.NewBeeMallCategoryService;
import com.demo.swagger.swagger.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NewBeeMallCategoryServiceImpl implements NewBeeMallCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public List<NewBeeMallIndexCategoryVO> getCategoriesForIndex() {
        List<NewBeeMallIndexCategoryVO> newBeeMallIndexCategoryVOS = new ArrayList<>();
        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L),
                        NewBeeMallCategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
            List<GoodsCategory> secondaryLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds,
                    NewBeeMallCategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if (!CollectionUtils.isEmpty(secondaryLevelCategories)) {
                List<Long> secondaryLevelCategoryIds = secondaryLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());
                List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondaryLevelCategoryIds,
                        NewBeeMallCategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if (!CollectionUtils.isEmpty(thirdLevelCategories)) {
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
                    List<SecondaryLevelCategoryVO> secondaryLevelCategoryVOS = new ArrayList<>();

                    for (GoodsCategory secondaryLevelCategory : secondaryLevelCategories) {
                        SecondaryLevelCategoryVO secondaryLevelCategoryVO = new SecondaryLevelCategoryVO();
                        BeanUtils.copyProperties(secondaryLevelCategory, secondaryLevelCategoryVO);
                        if (thirdLevelCategoryMap.containsKey(secondaryLevelCategoryVO.getCategoryId())) {
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondaryLevelCategoryVO.getCategoryId());
                            secondaryLevelCategoryVO.setThirdLevelCategoryVOS(com.demo.swagger.swagger.utils.BeanUtils.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class));
                            secondaryLevelCategoryVOS.add(secondaryLevelCategoryVO);
                        }
                    }

                    if (!CollectionUtils.isEmpty(secondaryLevelCategoryVOS)) {
                        Map<Long, List<SecondaryLevelCategoryVO>> secondaryLevelCategoryVOMap =
                                secondaryLevelCategoryVOS.stream().collect(groupingBy(SecondaryLevelCategoryVO::getParentId));

                        for (GoodsCategory firstLevelCategory : firstLevelCategories) {
                            NewBeeMallIndexCategoryVO newBeeMallIndexCategoryVO = new NewBeeMallIndexCategoryVO();
                            com.demo.swagger.swagger.utils.BeanUtils.copyProperties(firstLevelCategory, newBeeMallIndexCategoryVO);
                            if (secondaryLevelCategoryVOMap.containsKey(firstLevelCategory.getCategoryId())) {
                                List<SecondaryLevelCategoryVO> tempGoodsCategories = secondaryLevelCategoryVOMap.get(firstLevelCategory.getCategoryId());
                                newBeeMallIndexCategoryVO.setSecondaryLevelCategoryVOS(tempGoodsCategories);
                                newBeeMallIndexCategoryVOS.add(newBeeMallIndexCategoryVO);
                            }
                        }
                    }
                }
            }

            return newBeeMallIndexCategoryVOS;
        } else {
            return null;
        }
    }
}

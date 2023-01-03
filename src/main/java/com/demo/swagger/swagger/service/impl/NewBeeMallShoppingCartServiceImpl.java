package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.Constants;
import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.controller.param.SaveCartItemParam;
import com.demo.swagger.swagger.controller.param.UpdateCartItemParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallShoppingCartItemVO;
import com.demo.swagger.swagger.dao.NewBeeMallGoodsMapper;
import com.demo.swagger.swagger.dao.NewBeeMallShoppingCartItemMapper;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.NewBeeMallGoods;
import com.demo.swagger.swagger.entity.NewBeeMallShoppingCartItem;
import com.demo.swagger.swagger.service.NewBeeMallShoppingCartService;
import com.demo.swagger.swagger.utils.PageQueryUtils;
import com.demo.swagger.swagger.utils.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NewBeeMallShoppingCartServiceImpl implements NewBeeMallShoppingCartService {

    @Autowired
    private NewBeeMallShoppingCartItemMapper newBeeMallShoppingCartItemMapper;

    @Autowired
    private NewBeeMallGoodsMapper newBeeMallGoodsMapper;

    @Override
    public String saveNewBeeMallCartItem(SaveCartItemParam saveCartItemParam, Long userId) {
        NewBeeMallShoppingCartItem temp = newBeeMallShoppingCartItemMapper.selectByUserIdAndGoodsId(userId, saveCartItemParam.getGoodsId());
        if (temp != null) {
            NewBeeMallException.fail(ServiceResultEnum.SHOPPING_CART_ITEM_EXIST_ERROR.getResult());
        }

        NewBeeMallGoods newBeeMallGoods = newBeeMallGoodsMapper.selectByPrimaryKey(saveCartItemParam.getGoodsId());
        if (newBeeMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }

        int totalItem = newBeeMallShoppingCartItemMapper.selectCountByUserId(userId);
        if (saveCartItemParam.getGoodsCount() < 1) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_NUMBER_ERROR.getResult();
        }
        if (saveCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
        }

        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = new NewBeeMallShoppingCartItem();
        BeanUtils.copyProperties(saveCartItemParam, newBeeMallShoppingCartItem);
        newBeeMallShoppingCartItem.setUserId(userId);
        if (newBeeMallShoppingCartItemMapper.insertSelective(newBeeMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateNewBeeMallCartItem(UpdateCartItemParam updateCartItemParam, Long userId) {
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(updateCartItemParam.getCartItemId());
        if (newBeeMallShoppingCartItem == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (!newBeeMallShoppingCartItem.getUserId().equals(userId)) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }
        if (updateCartItemParam.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }

        newBeeMallShoppingCartItem.setGoodsCount(updateCartItemParam.getGoodsCount());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        newBeeMallShoppingCartItem.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
        if (newBeeMallShoppingCartItemMapper.updateByPrimaryKeySelective(newBeeMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String deleteNewBeeMallCartItem(Long cartItemId, MallUser mallUser) {
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = getNewBeeMallCartItemById(cartItemId);
        if (!newBeeMallShoppingCartItem.getUserId().equals(mallUser.getUserId())) {
            return ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult();
        }
        if (deleteById(cartItemId)) {
            return ServiceResultEnum.SUCCESS.getResult();
        }

        return ServiceResultEnum.OPERATE_ERROR.getResult();
    }

    @Override
    public PageResult getMyShoppingCartItems(PageQueryUtils pageQueryUtils) {
        List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems = newBeeMallShoppingCartItemMapper.findMyNewBeeCartItems(pageQueryUtils);
        int total = newBeeMallShoppingCartItemMapper.getTotalMyNewBeeMallCartItems(pageQueryUtils);

        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = getNewBeeMallShoppingCartItemVOS(newBeeMallShoppingCartItems);
        return new PageResult(newBeeMallShoppingCartItemVOS, total, pageQueryUtils.getLimit(), pageQueryUtils.getPage());
    }

    private List<NewBeeMallShoppingCartItemVO> getNewBeeMallShoppingCartItemVOS(List<NewBeeMallShoppingCartItem> newBeeMallShoppingCartItems) {
        List<NewBeeMallShoppingCartItemVO> newBeeMallShoppingCartItemVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(newBeeMallShoppingCartItems)) {
            List<Long> goodsIds = newBeeMallShoppingCartItems.stream().map(NewBeeMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<NewBeeMallGoods> newBeeMallGoods = newBeeMallGoodsMapper.selectGoodsByPrimaryKeys(goodsIds);

            Map<Long, NewBeeMallGoods> newBeeMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(newBeeMallGoods)) {
                newBeeMallGoodsMap = newBeeMallGoods.stream().collect(Collectors.toMap(NewBeeMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }

            for (NewBeeMallShoppingCartItem newBeeMallShoppingCartItem : newBeeMallShoppingCartItems) {
                NewBeeMallShoppingCartItemVO newBeeMallShoppingCartItemVO = new NewBeeMallShoppingCartItemVO();
                BeanUtils.copyProperties(newBeeMallShoppingCartItem, newBeeMallShoppingCartItemVO);
                if (newBeeMallGoodsMap.containsKey(newBeeMallShoppingCartItem.getGoodsId())) {
                    NewBeeMallGoods temp = newBeeMallGoodsMap.get(newBeeMallShoppingCartItem.getGoodsId());
                    newBeeMallShoppingCartItemVO.setGoodsCoverImg(temp.getGoodsCoverImg());
                    String goodsName = temp.getGoodsName();
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    newBeeMallShoppingCartItemVO.setGoodsName(goodsName);
                    newBeeMallShoppingCartItemVO.setSellingPrice(temp.getSellingPrice());
                    newBeeMallShoppingCartItemVOS.add(newBeeMallShoppingCartItemVO);
                }
            }
        }

        return newBeeMallShoppingCartItemVOS;
    }

    public NewBeeMallShoppingCartItem getNewBeeMallCartItemById(Long cartItemId) {
        NewBeeMallShoppingCartItem newBeeMallShoppingCartItem = newBeeMallShoppingCartItemMapper.selectByPrimaryKey(cartItemId);
        if (newBeeMallShoppingCartItem == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }

        return newBeeMallShoppingCartItem;
    }

    public Boolean deleteById(Long cartItemId) {
        return newBeeMallShoppingCartItemMapper.deleteByPrimaryKey(cartItemId) > 0;
    }
}

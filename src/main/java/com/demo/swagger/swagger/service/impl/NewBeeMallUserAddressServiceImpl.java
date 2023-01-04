package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.dao.MallUserAddressMapper;
import com.demo.swagger.swagger.entity.MallUserAddress;
import com.demo.swagger.swagger.service.NewBeeMallUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewBeeMallUserAddressServiceImpl implements NewBeeMallUserAddressService {

    @Autowired
    private MallUserAddressMapper mallUserAddressMapper;

    @Override
    public MallUserAddress getMallUserAddressById(Long addressId) {
        MallUserAddress mallUserAddress = mallUserAddressMapper.selectByPrimaryKey(addressId);
        if (mallUserAddress == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallUserAddress;
    }
}

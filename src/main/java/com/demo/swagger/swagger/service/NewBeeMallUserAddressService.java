package com.demo.swagger.swagger.service;

import com.demo.swagger.swagger.controller.param.SaveMallUserAddressParam;
import com.demo.swagger.swagger.controller.param.UpdateMallUserAddressParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallUserAddressVO;
import com.demo.swagger.swagger.entity.MallUserAddress;

import java.util.List;

public interface NewBeeMallUserAddressService {

    MallUserAddress getMallUserAddressById(Long addressId);

    List<NewBeeMallUserAddressVO> getMyAddress(Long userId);

    Boolean saveUserAddress(SaveMallUserAddressParam saveMallUserAddressParam, Long userId);

    Boolean updateMallUserAddress(UpdateMallUserAddressParam updateMallUserAddressParam, Long userId);

    MallUserAddress getMallUserAddressByUserId(Long userId);

    Boolean deleteByAddressId(Long addressId, Long userId);
}

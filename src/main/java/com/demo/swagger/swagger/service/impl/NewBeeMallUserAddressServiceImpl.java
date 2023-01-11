package com.demo.swagger.swagger.service.impl;

import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.controller.param.SaveMallUserAddressParam;
import com.demo.swagger.swagger.controller.param.UpdateMallUserAddressParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallUserAddressVO;
import com.demo.swagger.swagger.dao.MallUserAddressMapper;
import com.demo.swagger.swagger.entity.MallUserAddress;
import com.demo.swagger.swagger.service.NewBeeMallUserAddressService;
import com.demo.swagger.swagger.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<NewBeeMallUserAddressVO> getMyAddress(Long userId) {
        List<MallUserAddress> mallUserAddressList = mallUserAddressMapper.findMyAddressList(userId);
        List<NewBeeMallUserAddressVO> newBeeMallUserAddressVOS = BeanUtils.copyList(mallUserAddressList, NewBeeMallUserAddressVO.class);

        return newBeeMallUserAddressVOS;
    }

    @Override
    @Transactional
    public Boolean saveUserAddress(SaveMallUserAddressParam saveMallUserAddressParam, Long userId) {
        MallUserAddress mallUserAddress = new MallUserAddress();
        BeanUtils.copyProperties(saveMallUserAddressParam, mallUserAddress);
        mallUserAddress.setUserId(userId);

        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            MallUserAddress defaultAddress = mallUserAddressMapper.getMyDefaultAddress(userId);
            if (defaultAddress != null) {
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setIsDeleted((byte) 0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                defaultAddress.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
                int updateResult = mallUserAddressMapper.updateByPrimaryKeySelective(defaultAddress);
                if (updateResult < 1) {
                    NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }

        return mallUserAddressMapper.insertSelective(mallUserAddress) > 0;
    }

    @Override
    public Boolean updateMallUserAddress(UpdateMallUserAddressParam updateMallUserAddressParam, Long userId) {
        MallUserAddress addressBeforeUpdate = mallUserAddressMapper.selectByPrimaryKey(updateMallUserAddressParam.getAddressId());
        if (addressBeforeUpdate == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!addressBeforeUpdate.getUserId().equals(userId)) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }

        MallUserAddress addressAfterUpdate = new MallUserAddress();
        BeanUtils.copyProperties(updateMallUserAddressParam, addressAfterUpdate);
        addressAfterUpdate.setUserId(userId);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (updateMallUserAddressParam.getDefaultFlag().intValue() == 1) {
            MallUserAddress defaultAddress = mallUserAddressMapper.getMyDefaultAddress(userId);
            defaultAddress.setDefaultFlag((byte) 0);
            defaultAddress.setIsDeleted((byte) 0);
            defaultAddress.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
            int updateResult = mallUserAddressMapper.updateByPrimaryKeySelective(defaultAddress);
            if (updateResult < 1) {
                NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
        }

        addressAfterUpdate.setIsDeleted((byte) 0);
        addressAfterUpdate.setUpdateTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
        return mallUserAddressMapper.updateByPrimaryKeySelective(addressAfterUpdate) > 0;
    }

    @Override
    public MallUserAddress getMallUserAddressByUserId(Long userId) {
        return mallUserAddressMapper.getMyDefaultAddress(userId);
    }

    @Override
    public Boolean deleteByAddressId(Long addressId, Long userId) {
        MallUserAddress mallUserAddress = mallUserAddressMapper.selectByPrimaryKey(addressId);
        if (mallUserAddress == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        if (!mallUserAddress.getUserId().equals(userId)) {
            NewBeeMallException.fail(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }

        return mallUserAddressMapper.deleteByPrimaryKey(addressId) > 0;
    }
}

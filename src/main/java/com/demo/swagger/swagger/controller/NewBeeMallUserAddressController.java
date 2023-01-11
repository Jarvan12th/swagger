package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.config.annotation.TokenToMallUser;
import com.demo.swagger.swagger.controller.param.SaveMallUserAddressParam;
import com.demo.swagger.swagger.controller.param.UpdateMallUserAddressParam;
import com.demo.swagger.swagger.controller.vo.NewBeeMallUserAddressVO;
import com.demo.swagger.swagger.entity.MallUser;
import com.demo.swagger.swagger.entity.MallUserAddress;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallUserAddressService;
import com.demo.swagger.swagger.utils.BeanUtils;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "Address Api")
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", allowCredentials = "true")
public class NewBeeMallUserAddressController {

    @Resource
    private NewBeeMallUserAddressService newBeeMallUserAddressService;

    @ApiOperation(value = "My Address List", notes = "My Address List")
    @GetMapping("/address")
    public Result<List<NewBeeMallUserAddressVO>> addressList(@TokenToMallUser MallUser mallUser) {
        List<NewBeeMallUserAddressVO> newBeeMallUserAddressVOS = newBeeMallUserAddressService.getMyAddress(mallUser.getUserId());
        return ResultGenerator.generateSuccessResultWithData(newBeeMallUserAddressVOS);
    }

    @ApiOperation(value = "Add Address", notes = "Add Address")
    @PostMapping("/address")
    public Result<Boolean> saveUserAddress(@RequestBody SaveMallUserAddressParam saveMallUserAddressParam,
                                           @TokenToMallUser MallUser mallUser) {
        Boolean saveResult = newBeeMallUserAddressService.saveUserAddress(saveMallUserAddressParam, mallUser.getUserId());
        if (saveResult) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult("add address fail");
    }

    @ApiOperation(value = "Update Address", notes = "Update Address")
    @PutMapping("/address")
    public Result<Boolean> updateUserAddress(@RequestBody UpdateMallUserAddressParam updateMallUserAddressParam,
                                             @TokenToMallUser MallUser mallUser) {
        Boolean updateResult = newBeeMallUserAddressService.updateMallUserAddress(updateMallUserAddressParam, mallUser.getUserId());

        if (updateResult) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult("update address fail");
    }

    @ApiOperation(value = "Get Address By Id", notes = "Get Address By Id")
    @GetMapping("/address/{addressId}")
    public Result<NewBeeMallUserAddressVO> getMallUserAddressById(@PathVariable("addressId") Long addressId,
                                                                  @TokenToMallUser MallUser mallUser) {
        MallUserAddress mallUserAddress = newBeeMallUserAddressService.getMallUserAddressById(addressId);
        NewBeeMallUserAddressVO newBeeMallUserAddressVO = new NewBeeMallUserAddressVO();
        BeanUtils.copyProperties(mallUserAddress, newBeeMallUserAddressVO);
        if (!mallUserAddress.getUserId().equals(mallUser.getUserId())) {
            return ResultGenerator.generateFailResult(ServiceResultEnum.REQUEST_FORBIDEN_ERROR.getResult());
        }

        return ResultGenerator.generateSuccessResultWithData(newBeeMallUserAddressVO);
    }

    @ApiOperation(value = "Get Default Address", notes = "Get Default Address")
    @GetMapping("/address/default")
    public Result<MallUserAddress> getDefaultMallUserAddress(@TokenToMallUser MallUser mallUser) {
        MallUserAddress mallUserAddress = newBeeMallUserAddressService.getMallUserAddressByUserId(mallUser.getUserId());
        return ResultGenerator.generateSuccessResultWithData(mallUserAddress);
    }

    @ApiOperation(value = "Delete Address By Id", notes = "Delete address By Id")
    @DeleteMapping("/address/{addressId}")
    public Result deleteAddress(@PathVariable("addressId") Long addressId, @TokenToMallUser MallUser mallUser) {
        Boolean deleteResult = newBeeMallUserAddressService.deleteByAddressId(addressId, mallUser.getUserId());

        if (deleteResult) {
            return ResultGenerator.generateSuccessResult();
        }

        return ResultGenerator.generateFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }
}

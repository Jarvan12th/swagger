package com.demo.swagger.swagger.controller;

import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.controller.vo.NewBeeMallIndexCategoryVO;
import com.demo.swagger.swagger.entity.Result;
import com.demo.swagger.swagger.service.NewBeeMallCategoryService;
import com.demo.swagger.swagger.utils.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "Category Page Api")
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:8081", allowedHeaders = "*", allowCredentials = "true")
public class NewBeeMallGoodsCategoryController {

    @Resource
    private NewBeeMallCategoryService newBeeMallCategoryService;

    @ApiOperation(value = "Get Category", notes = "Get Category")
    @GetMapping("/categories")
    public Result<List<NewBeeMallIndexCategoryVO>> getCategories() {
        List<NewBeeMallIndexCategoryVO> newBeeMallIndexCategoryVOS = newBeeMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(newBeeMallIndexCategoryVOS)) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }

        return ResultGenerator.generateSuccessResultWithData(newBeeMallIndexCategoryVOS);
    }
}

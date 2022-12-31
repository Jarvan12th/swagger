package com.demo.swagger.swagger.config;

import com.demo.swagger.swagger.common.NewBeeMallException;
import com.demo.swagger.swagger.common.ServiceResultEnum;
import com.demo.swagger.swagger.entity.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NewBeeMallExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        Result result = new Result(500, e.getMessage());

        if (e instanceof NewBeeMallException) {
            if (ServiceResultEnum.NOT_LOGIN_ERROR.getResult().equals(e.getMessage()) ||
            ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult().equals(e.getMessage())) {
                result.setResultCode(416);
            }
        } else {
            e.printStackTrace();
            result.setMessage("Unexpected Exception");
        }

        return result;
    }
}

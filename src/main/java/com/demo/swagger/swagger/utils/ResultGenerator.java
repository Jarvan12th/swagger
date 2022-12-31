package com.demo.swagger.swagger.utils;

import com.demo.swagger.swagger.entity.Result;
import org.springframework.util.StringUtils;

public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int RESULT_CODE_SUCCESS = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static Result generateSuccessResult() {
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result generateSuccessResult(String message) {
        return new Result(RESULT_CODE_SUCCESS, message);
    }

    public static Result generateSuccessResult(Object data) {
        return new Result(RESULT_CODE_SUCCESS, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static Result generateFailResult(String message) {
        return new Result(RESULT_CODE_SERVER_ERROR, StringUtils.isEmpty(message) ? DEFAULT_FAIL_MESSAGE : message);
    }

    public static Result generateErrorResult(int resultCode, String message) {
        return new Result(resultCode, message);
    }
}

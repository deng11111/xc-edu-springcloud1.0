package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * Created by mrt on 2018/3/7.
 */
public final class ExceptionCast {

    private ExceptionCast() {
    }

    //抛出自定义异常
    public static void cast(ResultCode resultCode) {
        throw new CustomException(resultCode);
    }
    //抛出自定义异常，带异常处理对象
    public static void cast(ResultCode resultCode,ExceptionProcess exceptionHandler) {
        throw new CustomException(resultCode,exceptionHandler);
    }

}
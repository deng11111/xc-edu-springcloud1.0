package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResponseResult;

/**
 * Created by mrt on 2018/3/7.
 */
public abstract class  ExceptionProcess {
    abstract ResponseResult handler();
}

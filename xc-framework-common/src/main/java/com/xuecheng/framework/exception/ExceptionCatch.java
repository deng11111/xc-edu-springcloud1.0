package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by mrt on 2018/3/7.
 */
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    /**
     * 捕获CommonException异常，输出 异常信息
     */
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public ResponseResult CommonException(Exception e, HttpServletRequest request) {
        LOGGER.error("catch exception id: {}\r\nexception: {}",e.getMessage(), e);
         CustomException customException = (CustomException)e;
        ExceptionProcess exceptionProcess = customException.getExceptionProcess();
        if(exceptionProcess==null){
            //默认将异常信息响应给用户
            ResultCode resultCode = customException.getResultCode();
            final ResponseResult responseResult = new ResponseResult(resultCode);
            return responseResult;
        }else{
            //如果定义异常处理器则执行异常处理
            return exceptionProcess.handler();
        }

    }
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult exception(Exception e, HttpServletRequest request) {
        LOGGER.error("1catch exception id: {}\r\nexception: ",e.getMessage(), e);
        if(EXCEPTIONS == null)
            EXCEPTIONS = builder.build();
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if (resultCode != null) {
            responseResult = new ResponseResult(resultCode);
        } else {
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    static {
    //向builder中填入通用的异常类及错误代码

    }
}

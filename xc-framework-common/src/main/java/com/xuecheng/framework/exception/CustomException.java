package com.xuecheng.framework.exception;


import com.xuecheng.framework.model.response.ResultCode;
/**
 * Created by mrt on 2018/3/7.
 */
public class CustomException extends RuntimeException {

    private ResultCode resultCode;
    private ExceptionProcess exceptionProcess;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(ResultCode resultCode) {
        this(String.valueOf(resultCode.code())+","+resultCode.message());
        this.resultCode = resultCode;
    }
    public CustomException(ResultCode resultCode, ExceptionProcess exceptionProcess) {
        this(String.valueOf(resultCode.code()));
        this.resultCode = resultCode;
        this.exceptionProcess = exceptionProcess;
    }

    public ResultCode getResultCode(){
        return this.resultCode;
    }
    public ExceptionProcess getExceptionProcess(){
        return this.exceptionProcess;
    }
}

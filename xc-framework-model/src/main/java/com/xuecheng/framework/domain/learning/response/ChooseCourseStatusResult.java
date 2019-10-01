package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 16:45
 **/
@Data
@ToString
@NoArgsConstructor
public class ChooseCourseStatusResult extends ResponseResult {
    public ChooseCourseStatusResult(ResultCode resultCode,String status) {
        super(resultCode);
        this.status = status;
    }
    private String status;



}

package com.xuecheng.api.learning.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2018/3/5.
 */
@ToString
public enum LearningCode implements ResultCode {
    CHOOSECOURSE_COURSEISNULL(false,24001,"选课失败，没有确定课程！"),
    CHOOSECOURSE_ADDOPENCOURSE_CHARGE(false,24002,"报名失败请购买课程！"),
    LEARNING_GETMEDIA_NOTFOUND(false,24101,"无法获取播放地址，请与管理员联系！"),
    LEARNING_GETTEACHPLAN_ERROR(false,24102,"无法获取课程章节信息！"),
    LEARNING_GETCOURSE_ERROR(false,24103,"无法获取课程信息！"),
    LEARNING_GETMEDIA_ERROR(false,24104,"无法获取课程资源！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private LearningCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, LearningCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, LearningCode> builder = ImmutableMap.builder();
        for (LearningCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

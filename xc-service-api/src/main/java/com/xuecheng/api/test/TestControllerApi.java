package com.xuecheng.api.test;

import com.xuecheng.framework.domain.test.UserTest;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

/**
 @Api：修饰整个类，描述Controller的作用
 @ApiOperation：描述一个类的一个方法，或者说一个接口
 @ApiParam：单个参数描述
 @ApiModel：用对象来接收参数
 @ApiProperty：用对象接收参数时，描述对象的一个字段
 @ApiResponse：HTTP响应其中1个描述
 @ApiResponses：HTTP响应整体描述
 @ApiIgnore：使用该注解忽略这个API
 @ApiError ：发生错误返回的信息
 @ApiImplicitParam：一个请求参数
 @ApiImplicitParams：多个请求参数
 */
//@RequestMapping("/test")
@Api(value = "test API测试",description = "test API测试",tags = {"test模块"})
public interface TestControllerApi {

    final String API_PRE = "/test";

    @GetMapping(API_PRE+"/get/{id}")
    @ApiOperation("通过ID查询测试")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "String")
    })
    public UserTest get(@PathVariable("id") String id) throws IOException;



}

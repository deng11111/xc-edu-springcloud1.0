package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 接口定义规范：
 * 1、类上统一不使用requestMapping。（此接口将来用于Feign client父接口，而Feign client不支持类上带requestmapping）
 * 2、@PathVariable 统一指定参数名称，如：@PathVariable("id")
 * 3、接口定义完成由于要生成Swagger接口，在同包下创建对应类型类且使用@RestController标识（方法为空即可）
 */

@Api(value = "用户认证",description = "用户认证接口",tags = {"用户认证接口"})
public interface AuthControllerApi {
    final String API_PRE = "";

    @PostMapping(API_PRE+"/userlogin")
    @ApiOperation("登录")
    public LoginResult login(LoginRequest loginRequest);

    @GetMapping(API_PRE+"/userjwt")
    @ApiOperation("查询userjwt令牌")
    public JwtResult userjwt();

    @PostMapping(API_PRE+"/userlogout")
    @ApiOperation("退出")
    public ResponseResult logout();

}

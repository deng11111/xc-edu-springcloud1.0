package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.UserBasicInfo;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 接口定义规范：
 * 1、类上统一不使用requestMapping。（此接口将来用于Feign client父接口，而Feign client不支持类上带requestmapping）
 * 2、@PathVariable 统一指定参数名称，如：@PathVariable("id")
 * 3、接口定义完成由于要生成Swagger接口，在同包下创建对应类型类且使用@RestController标识（方法为空即可）
 */

@Api(value = "用户中心",description = "用户中心管理",tags = {"用户中心管理"})
public interface UcenterControllerApi {
    final String API_PRE = "/ucenter";

    @GetMapping(API_PRE+"/getuserext")
    public XcUserExt getUserext(@RequestParam("username") String username);

    @GetMapping(API_PRE+"/getuserbasic")
    public UserBasicInfo getUserbasic();

}

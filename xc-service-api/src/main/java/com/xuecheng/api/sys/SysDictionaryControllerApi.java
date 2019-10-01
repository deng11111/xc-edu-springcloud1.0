package com.xuecheng.api.sys;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: mrt.
 * @Description:
 * @Date:Created in 2018/1/24 11:17.
 * @Modified By:
 */
@RequestMapping("/sys/dictionary")
@Api(value = "系统数据字典管理接口",description = "系统数据字典管理接口",tags = {"系统数据字典管理接口"})
public interface SysDictionaryControllerApi {
    @GetMapping("/get/{id}")
    @ApiOperation("通过字典分类id查询数据字典")
    @ResponseBody
    public SysDictionary getById(@PathVariable("id") String id) ;

}

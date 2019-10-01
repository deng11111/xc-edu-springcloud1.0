package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * 接口定义规范：
 * 1、类上统一不使用requestMapping。（此接口将来用于Feign client父接口，而Feign client不支持类上带requestmapping）
 * 2、@PathVariable 统一指定参数名称，如：@PathVariable("id")
 * 3、接口定义完成由于要生成Swagger接口，在同包下创建对应类型类且使用@RestController标识（方法为空即可）
 */

@Api(value = "cms页面管理",description = "cms页面管理接口",tags = {"cms页面管理接口"})
public interface CmsPageControllerApi {
    final String API_PRE = "/cms/page";

    @PostMapping(API_PRE+"/add")
    @ApiOperation("添加页面")
    public CmsPageResult add(@RequestBody CmsPage cmsPage);

    @DeleteMapping(API_PRE+"/del/{id}")
    @ApiOperation("通过ID删除页面")
    public ResponseResult delete(@PathVariable("id") String id);

    @GetMapping(API_PRE+"/get/{id}")
    @ApiOperation("通过ID查询页面")
    public CmsPageResult findById(@PathVariable("id") String id) ;

    @PutMapping(API_PRE+"/edit")
    @ApiOperation("更新页面")
    public CmsPageResult update(@RequestBody CmsPage cmsPage);

    @GetMapping(API_PRE+"/list/{page}/{size}")
    @ApiOperation("查询页面列表")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size,QueryPageRequest queryPageRequest) ;

    @PostMapping(API_PRE+"/generateHtml/{pageId}")
    @ApiOperation("生成静态化页面")
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId) throws Exception;

    @GetMapping(API_PRE+"/getHtml/{pageId}")
    @ApiOperation("查询静态页面")
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId) throws IOException;

    @PostMapping(API_PRE+"/postPage/{pageId}")
    @ApiOperation("发布页面")
    public ResponseResult post(@PathVariable("pageId") String pageId);
}

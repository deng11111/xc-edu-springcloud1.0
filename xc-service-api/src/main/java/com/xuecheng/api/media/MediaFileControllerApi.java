package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.domain.media.response.MediaFileResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * 接口定义规范：
 * 1、类上统一不使用requestMapping。（此接口将来用于Feign client父接口，而Feign client不支持类上带requestmapping）
 * 2、@PathVariable 统一指定参数名称，如：@PathVariable("id")
 * 3、接口定义完成由于要生成Swagger接口，在同包下创建对应类型类且使用@RestController标识（方法为空即可）
 */

@Api(value = "媒体文件管理",description = "媒体文件管理接口",tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {
    final String API_PRE = "/media/file";

    @DeleteMapping(API_PRE+"/del/{id}")
    @ApiOperation("通过ID删除文件")
    public ResponseResult delete(@PathVariable("id") String id);

    @GetMapping(API_PRE+"/get/{id}")
    @ApiOperation("通过ID查询文件")
    public MediaFileResult findById(@PathVariable("id") String id) ;

    @PutMapping(API_PRE+"/edit/{id}")
    @ApiOperation("更新文件")
    public MediaFileResult update(@PathVariable("id") String id,@RequestBody MediaFile mediaFile);

    @GetMapping(API_PRE+"/list/{page}/{size}")
    @ApiOperation("查询文件列表")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) ;

    @PostMapping(API_PRE+"/search")
    @ApiOperation("根据原始文件名称查询文件")
    public QueryResponseResult findList(@RequestParam("fileOriginalName") String fileOriginalName) ;

    @PostMapping(API_PRE+"/process/{id}")
    @ApiOperation("开始处理某个文件")
    public ResponseResult process(@PathVariable("id") String id) ;
}

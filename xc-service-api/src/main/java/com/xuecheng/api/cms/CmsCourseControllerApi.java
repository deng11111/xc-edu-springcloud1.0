package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * 接口定义规范：
 * 1、类上统一不使用requestMapping。（此接口将来用于Feign client父接口，而Feign client不支持类上带requestmapping）
 * 2、@PathVariable 统一指定参数名称，如：@PathVariable("id")
 * 3、接口定义完成由于要生成Swagger接口，在同包下创建对应类型类且使用@RestController标识（方法为空即可）
 */

@Api(value = "cms课程接口", description = "cms课程接口", tags = {"cms课程接口"})
public interface CmsCourseControllerApi {
    final String API_PRE = "/cms/course";

    @PostMapping(API_PRE + "/cpreview")
    @ApiOperation("生成课程预览页")
    public CoursePreviewResult cpreview(@RequestParam("courseId") String courseId);

    @PostMapping(API_PRE + "/checkcpreview")
    @ApiOperation("校验是否生成课程预览页")
    public CoursePreviewResult checkcpreview(@RequestParam("courseId") String courseId);

    @PostMapping(API_PRE + "/cdetail")
    @ApiOperation("生成课程详情页面")
    public ResponseResult cdetail(@RequestParam("courseId") String courseId);
}

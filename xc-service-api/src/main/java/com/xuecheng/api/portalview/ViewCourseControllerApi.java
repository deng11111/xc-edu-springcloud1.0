package com.xuecheng.api.portalview;

import com.xuecheng.framework.domain.portalview.PreViewCourse;
import com.xuecheng.framework.domain.portalview.PreViewCourseMedia;
import com.xuecheng.framework.domain.portalview.ViewCourse;
import com.xuecheng.framework.domain.portalview.ViewCourseMedia;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: mrt.
 * @Description: 数据视图服务接口
 * @Date:Created in 2018/1/24 11:17.
 * @Modified By:
 */

@Api(value = "课程数据视图服务",description = "门户课程数据视图管理接口")
public interface ViewCourseControllerApi {
    final String API_PRE = "";

    @PostMapping(API_PRE+"/add/{courseId}")
    @ApiOperation("添加课程视图")
    public ResponseResult add(@PathVariable("courseId") String courseId, @RequestBody PreViewCourse preViewCourse);

    @GetMapping(API_PRE+"/getpre/{id}")
    @ApiOperation("通过课程ID查询课程视图")
    public PreViewCourse findpreById(@PathVariable("id") String id) ;

    @GetMapping(API_PRE+"/get/{id}")
    @ApiOperation("通过课程ID查询课程视图")
    public ViewCourse findById(@PathVariable("id") String id) ;

    @PostMapping(API_PRE+"/publish/{courseId}")
    @ApiOperation("发布课程视图")
    public ResponseResult publish(@PathVariable("courseId") String courseId);


    @PostMapping(API_PRE+"/addmedia/{courseId}")
    @ApiOperation("添加课程媒资视图")
    public ResponseResult addmedia(@PathVariable("courseId") String courseId, @RequestBody List<PreViewCourseMedia> preViewCourseMedias);

    @GetMapping(API_PRE+"/getmedia/{id}")
    @ApiOperation("通过教学计划id查询课程媒资视图")
    public ViewCourseMedia findMediaById(@PathVariable("id") String id) ;
}

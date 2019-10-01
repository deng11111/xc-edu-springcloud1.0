package com.xuecheng.api.course;

import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanExt;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mrt on 2018/2/7.
 */

@RequestMapping("/course")
@Api(value = "课程管理",description = "课程管理",tags = {"课程管理"})
public interface CourseControllerApi {

    @PostMapping("/preview/{id}")
    @ApiOperation("预览课程")
    public CoursePreviewResult preview(@PathVariable("id") String id);

    @PostMapping("/checkpreview/{id}")
    @ApiOperation("校验是否预览课程")
    public CoursePreviewResult checkpreview(@PathVariable("id") String id);


    @PostMapping("/publish/{id}")
    @ApiOperation("发布课程")
    public ResponseResult publish(@PathVariable("id") String id);

    @PostMapping("/coursebase/add")
    @ApiOperation("添加课程基础信息")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase);

    /***********************************课程图片*********************************/
    @PostMapping("/coursepic/add")
    @ApiOperation("添加课程图片")
    public ResponseResult addCoursePic(@RequestParam("courseId")String courseId,@RequestParam("pic")String pic);

    @DeleteMapping("/coursepic/delete")
    @ApiOperation("删除课程图片")
    public ResponseResult deleteCoursePic(@RequestParam("courseId")String courseId);

    @GetMapping("/coursepic/list/{courseId}")
    @ApiOperation("获取课程基础信息")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId);

    //查询课程列表
    @GetMapping("/coursebase/list/{page}/{size}")
    @ApiOperation("查询我的课程列表")
    @ResponseBody
    public QueryResult<CourseInfo> findCourseList(
            @PathVariable("page") int page,
            @PathVariable("size") int size,
            @RequestBody CourseListRequest courseListRequest
    );

    @GetMapping("/coursebase/get/{courseId}")
    @ApiOperation("获取课程基础信息")
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException;

    @PutMapping("/coursebase/update/{id}")
    @ApiOperation("更新课程基础信息")
    public AddCourseResult updateCourseBase(@PathVariable("id") String id,@RequestBody CourseBase courseBase);

    /***********************************课程营销*********************************/
    @PostMapping("/coursemarket/update/{id}")
    @ApiOperation("更新课程营销信息")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id,@RequestBody CourseMarket courseMarket);

    @GetMapping("/coursemarket/get/{courseId}")
    @ApiOperation("获取课程营销信息")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId);
    /***********************************课程计划*********************************/
    @GetMapping("/teachplan/list/{courseId}")
    @ApiOperation("查询课程计划")
    @ResponseBody
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId);

    @PostMapping("/teachplan/add")
    @ApiOperation("添加课程计划(courseid,pname,parentid必填)")
    @ResponseBody
    public Teachplan addTeachplan(@RequestBody Teachplan teachplan);

    @GetMapping("/teachplan/get/{id}")
    @ApiOperation("获取课程计划")
    public TeachplanExt getTeachplanById(@PathVariable("id") String id);

    @PutMapping("/teachplan/update/{id}")
    @ApiOperation("更新课程计划")
    public ResponseResult updateTeachplan(@PathVariable("id") String id,@RequestBody TeachplanExt teachplanExt);

    @DeleteMapping("/teachplan/delete/{id}")
    @ApiOperation("删除课程计划")
    public ResponseResult deleteTeachplan(@PathVariable("id") String id);

}

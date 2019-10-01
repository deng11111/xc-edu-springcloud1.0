package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

/**
 * Created by mrt on 2018/2/7.
 */


@Api(value = "课程搜索",description = "课程搜索")
public interface EsCourseControllerApi {
    final String API_PRE = "";

    @GetMapping(API_PRE+"/list/{page}/{size}")
    @ApiOperation("课程搜索")
    public  Map<String,Object> list(@PathVariable("page") int page,
                        @PathVariable("size") int size,
                     @RequestParam("keyword") String keyword,
                     @RequestParam("mt") String mt,
                     @RequestParam("st") String st,
                     @RequestParam("grade") String grade) throws IOException;

    @GetMapping(value=API_PRE+"/getbase/{ids}")
    @ApiOperation("根据id查询课程信息")
    public Map<String,CourseInfo> getbase(@PathVariable("ids") String ids) throws IOException;

    @GetMapping(value=API_PRE+"/getall/{id}")
    @ApiOperation("根据id查询课程信息")
    public Map<String,CoursePub> getall(@PathVariable("id") String id) throws IOException;

}
